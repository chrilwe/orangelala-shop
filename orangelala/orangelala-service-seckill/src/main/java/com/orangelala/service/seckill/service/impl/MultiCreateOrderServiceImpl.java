package com.orangelala.service.seckill.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.model.seckill.SeckillDetail;
import com.orangelala.framework.model.seckill.SeckillOrderMessage;
import com.orangelala.framework.model.seckill.SeckillStatus;
import com.orangelala.service.seckill.configs.Status;
import com.orangelala.service.seckill.execption.SeckillException;
import com.orangelala.service.seckill.execption.msg.SeckillExceptionMsg;
import com.orangelala.service.seckill.service.MultiCreateOrderService;

@Service
public class MultiCreateOrderServiceImpl implements MultiCreateOrderService {
	
	private static final String SECKILL_DETAIL_QUEUE = "seckill_detail_queue";
	private static final String SECKILL_STATUS = "seckill_status_";
	private static final String SECKILL_ORDER_ID = "seckill_order_ID";
	private static final String SECKILL_ITEM_INVENTORY = "seckil_item_inventory_";
	private static final String SECKILL_ORDER_LIST = "seckill_order_list";
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	//异步下单
	@Override
	public void createOrder() {
		//从等待队列中获取秒杀信息
		String seckillMessage = stringRedisTemplate.opsForList().rightPop(SECKILL_DETAIL_QUEUE);
		if(StringUtils.isEmpty(seckillMessage)) {
			return;
		}
		SeckillDetail seckillDetail = JSON.parseObject(seckillMessage, SeckillDetail.class);
		
		//抢购状态：抢购中
		SeckillStatus seckillStatus = (SeckillStatus) stringRedisTemplate.opsForHash().get(SECKILL_STATUS, seckillDetail.getUserID());
		if(seckillStatus == null) {
			return;
		} else {
			if(seckillStatus.equals(Status.KILL_FAIL)) {
				return;
			}
			seckillStatus.setStatus(Status.KILLING);
			stringRedisTemplate.opsForHash().put(SECKILL_STATUS+seckillDetail.getUserID(), seckillDetail.getItemID(), seckillStatus);
		}
		
		//创建订单信息
		String orderId = stringRedisTemplate.opsForList().rightPop(SECKILL_ORDER_ID);
		
		SeckillOrderMessage seckillOrderMessage = new SeckillOrderMessage();
		seckillOrderMessage.setOrderID(orderId);
		seckillOrderMessage.setCreateTime(new Date());
		seckillOrderMessage.setItemId(seckillDetail.getItemID());
		seckillOrderMessage.setUserID(seckillDetail.getUserID());
		stringRedisTemplate.opsForHash().put(SECKILL_ORDER_LIST, orderId, seckillOrderMessage);
		
		//减少库存
		Long inventory = stringRedisTemplate.opsForValue().increment(SECKILL_ITEM_INVENTORY+seckillDetail.getItemID(), -1);
		if(inventory <= 0) {
			seckillStatus.setStatus(Status.KILL_FAIL);
			stringRedisTemplate.opsForHash().put(SECKILL_STATUS+seckillDetail.getUserID(), seckillDetail.getItemID(), seckillStatus);
			throw new SeckillException(SeckillExceptionMsg.SECKILL_END);
		}
		
		//抢购状态： 抢购成功
		if(!seckillStatus.equals(Status.KILL_FAIL)) {
			seckillStatus.setStatus(Status.KILL_SUCCESS);
			stringRedisTemplate.opsForHash().put(SECKILL_STATUS+seckillDetail.getUserID(), seckillDetail.getItemID(), seckillStatus);
		}
	}

}
