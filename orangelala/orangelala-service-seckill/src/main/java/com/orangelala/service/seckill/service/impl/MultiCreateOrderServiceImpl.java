package com.orangelala.service.seckill.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.model.seckill.SeckillDetail;
import com.orangelala.framework.model.seckill.SeckillItemInfo;
import com.orangelala.framework.model.seckill.SeckillOrderMessage;
import com.orangelala.framework.model.seckill.SeckillStatus;
import com.orangelala.service.seckill.configs.Status;
import com.orangelala.service.seckill.execption.SeckillException;
import com.orangelala.service.seckill.execption.msg.SeckillExceptionMsg;
import com.orangelala.service.seckill.service.MessageService;
import com.orangelala.service.seckill.service.MultiCreateOrderService;
import com.rabbitmq.client.Channel;

@Service
public class MultiCreateOrderServiceImpl implements MultiCreateOrderService {

	private static final String SECKILL_DETAIL_QUEUE = "seckill_detail_queue_";
	private static final String SECKILL_STATUS = "seckill_status_";
	private static final String SECKILL_ORDER_ID = "seckill_order_ID";
	private static final String SECKILL_ITEM_INVENTORY = "seckil_item_inventory_";
	private static final String SECKILL_ORDER_LIST = "seckill_order_list";
	private static final String SECKILL_USER_ITEM_ORDERS = "seckill_user_item_orders_";
	private static final String SECKILL_ITEMINFO = "seckill_iteminfo_";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private MessageService messageService;

	// 异步下单
	@Override
	public void createOrder(String itemId) {
		// 从等待队列中获取秒杀信息
		String seckillMessage = null;
		try {
			seckillMessage = stringRedisTemplate.opsForList().rightPop(SECKILL_DETAIL_QUEUE + itemId);
		} catch (Exception e1) {
			//取出失败，尝试重新取出
			for(int i = 0; i < 10; i++) {
				try {
					seckillMessage = stringRedisTemplate.opsForList().rightPop(SECKILL_DETAIL_QUEUE + itemId);
					break;
				} catch (Exception e) {
					continue;
				}
			}
		}
		if (StringUtils.isEmpty(seckillMessage)) {
			return;
		}
		
		String orderId = "";
		SeckillDetail seckillDetail = JSON.parseObject(seckillMessage, SeckillDetail.class);
		try {
			// 抢购状态：抢购中
			SeckillStatus seckillStatus = (SeckillStatus) stringRedisTemplate.opsForHash().get(SECKILL_STATUS,
					seckillDetail.getUserID());
			if (seckillStatus == null) {
				return;
			} else {
				if (seckillStatus.equals(Status.KILL_FAIL)) {
					return;
				}
				seckillStatus.setStatus(Status.KILLING);
				stringRedisTemplate.opsForHash().put(SECKILL_STATUS + seckillDetail.getUserID(),
						seckillDetail.getItemID(), seckillStatus);
			}
			
			//获取秒杀商品信息
			String string = stringRedisTemplate.opsForValue().get(SECKILL_ITEMINFO+seckillDetail.getItemID());
			if(StringUtils.isEmpty(string)) {
				return;
			}
			SeckillItemInfo seckillItemInfo = JSON.parseObject(string, SeckillItemInfo.class);
			
			// 减少库存
			Long inventory = stringRedisTemplate.opsForValue()
					.increment(SECKILL_ITEM_INVENTORY + seckillDetail.getItemID(), -1);
			if (inventory <= 0) {
		
				throw new SeckillException(SeckillExceptionMsg.SECKILL_END);
			}

			// 创建订单信息
			orderId = stringRedisTemplate.opsForList().rightPop(SECKILL_ORDER_ID);

			SeckillOrderMessage seckillOrderMessage = new SeckillOrderMessage();
			seckillOrderMessage.setOrderID(orderId);
			seckillOrderMessage.setCreateTime(new Date());
			seckillOrderMessage.setItemId(seckillDetail.getItemID());
			seckillOrderMessage.setUserID(seckillDetail.getUserID());
			seckillOrderMessage.setStatus(Status.PAY_NO);
			seckillOrderMessage.setPayTimeLimitSec(seckillItemInfo.getPayTimeLimitSec());
			stringRedisTemplate.opsForHash().put(SECKILL_ORDER_LIST, orderId, seckillOrderMessage);
			stringRedisTemplate.opsForHash().put(SECKILL_USER_ITEM_ORDERS+seckillDetail.getUserID(), seckillDetail.getItemID(), seckillOrderMessage);
			
			// 抢购状态： 抢购成功
			if (!seckillStatus.equals(Status.KILL_FAIL)) {
				seckillStatus.setStatus(Status.KILL_SUCCESS);
				stringRedisTemplate.opsForHash().put(SECKILL_STATUS + seckillDetail.getUserID(),
						seckillDetail.getItemID(), seckillStatus);
			}
			
			//MQ通知后台下单
			messageService.sendMessage(orderId);
		} catch (Exception e) {
			exceptionHandle(itemId, seckillDetail.getUserID(), orderId);
		} 
	}

	private void exceptionHandle(String itemId, String userId, String orderId) {
		if(StringUtils.isEmpty(orderId)) {
			updateSeckillStatus(itemId, userId, Status.KILL_FAIL);
		} else {
			//成功创建订单，但是之后未知异常发生，导致抢购未能成功set, 重新更新抢购状态
			SeckillOrderMessage orderMessage = (SeckillOrderMessage) stringRedisTemplate.opsForHash().get(SECKILL_ORDER_LIST, orderId);
			SeckillStatus seckillStatus = (SeckillStatus) stringRedisTemplate.opsForHash().get(SECKILL_STATUS+userId, itemId);
			String status = seckillStatus.getStatus();
			if(orderMessage != null && !status.equals(Status.KILL_SUCCESS)) {
				Object object = stringRedisTemplate.opsForHash().get(SECKILL_USER_ITEM_ORDERS+userId, itemId);
				if(object == null) {
					stringRedisTemplate.opsForHash().put(SECKILL_USER_ITEM_ORDERS+userId, itemId, orderMessage);
				}
				updateSeckillStatus(itemId, userId, Status.KILL_SUCCESS);
			} else if (orderMessage == null) {
				updateSeckillStatus(itemId, userId, Status.KILL_FAIL);
			}
		}
	}
	
	private void updateSeckillStatus(String itemId, String userId, String status) {
		SeckillStatus seckillStatus = (SeckillStatus) stringRedisTemplate.opsForHash().get(SECKILL_STATUS+userId, itemId);
		seckillStatus.setStatus(status);
		stringRedisTemplate.opsForHash().put(SECKILL_STATUS + userId,
				itemId, seckillStatus);
	}
	
}
