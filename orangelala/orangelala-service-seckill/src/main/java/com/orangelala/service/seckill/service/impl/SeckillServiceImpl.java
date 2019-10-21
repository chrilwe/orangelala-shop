package com.orangelala.service.seckill.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.response.msg.ItemMsg;
import com.orangelala.framework.model.item.Item;
import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.model.seckill.SeckillDetail;
import com.orangelala.framework.model.seckill.SeckillItemInfo;
import com.orangelala.framework.model.seckill.SeckillOrderMessage;
import com.orangelala.framework.model.seckill.SeckillStatus;
import com.orangelala.framework.utils.GenerateNum;
import com.orangelala.service.seckill.annotation.CheckSeckillStart;
import com.orangelala.service.seckill.configs.Status;
import com.orangelala.service.seckill.execption.SeckillCode;
import com.orangelala.service.seckill.execption.SeckillException;
import com.orangelala.service.seckill.execption.msg.SeckillExceptionMsg;
import com.orangelala.service.seckill.mapper.SeckillItemInfoMapper;
import com.orangelala.service.seckill.service.MultiCreateOrderService;
import com.orangelala.service.seckill.service.SeckillService;
import com.orangelala.service.seckill.task.SeckillStartUpJob;
import com.orangelala.service.seckill.threadpool.ThreadPoolBuilder;

@Service
public class SeckillServiceImpl implements SeckillService {
	
	private static final String SECKILL_TIMES = "seckill_times_";
	private static final String SECKILL_DETAIL_QUEUE = "seckill_detail_queue_";
	private static final String SECKILL_STATUS = "seckill_status_";
	private static final String SECKILL_ITEM_INVENTORY = "seckil_item_inventory_";
	private static final String SECKILL_ITEMINFO = "seckill_iteminfo_";
	private static final String SECKILL_ORDER_LIST = "seckill_order_list";
	private static final String SECKILL_USER_ITEM_ORDERS = "seckill_user_item_orders_";
	
	private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private MultiCreateOrderService multiCreateOrderService;
	@Autowired
	private SeckillItemInfoMapper seckillItemInfoMapper;

	@Override
	@CheckSeckillStart
	public BaseResponse seckill(String itemId, String userId) {
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(Code.PARAM_NULL,ItemMsg.ITEM_ID_NULL);
		}
		
		//一个用户只能抢购一次，不能重复抢购
		Long increment = stringRedisTemplate.opsForValue().increment(SECKILL_TIMES+userId+itemId, 1l);
		if(increment > 1l) {
			//不能重复抢购
			throw new SeckillException(SeckillExceptionMsg.SECKILL_ONLY_ONCE);
		}
		
		//将抢购请求封装，然后放到队列中排队
		Date currentTime = new Date();
		SeckillDetail seckillDetail = new SeckillDetail();
		seckillDetail.setItemID(itemId);
		seckillDetail.setRequestTime(currentTime);
		seckillDetail.setUserID(userId);
		stringRedisTemplate.opsForList().leftPush(SECKILL_DETAIL_QUEUE+itemId, JSON.toJSONString(seckillDetail));
		
		//设置抢购状态标识 : 排队中
		SeckillStatus seckillStatus = new SeckillStatus();
		seckillStatus.setCreateTime(currentTime);
		seckillStatus.setItemID(itemId);
		seckillStatus.setUserID(userId);
		seckillStatus.setStatus(Status.WAITING_IN_QUEUE);
		stringRedisTemplate.opsForHash().put(SECKILL_STATUS+userId, itemId, seckillStatus);
		
		//异步进行多线程下单
		ThreadPoolBuilder.build(20, 30, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), "MultiCreateOrder")
		.execute(new Runnable(){

			@Override
			public void run() {
				multiCreateOrderService.createOrder(itemId);
			}
			
		});
		
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//创建秒杀活动
	@Override
	@Transactional
	public BaseResponse createSeckillActivity(SeckillItemInfo seckillItemInfo) {
		if(seckillItemInfo == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		
		GenerateNum generate = new GenerateNum();
		String itemId = generate.generate();
		
		seckillItemInfo.setItemID(itemId);
		seckillItemInfo.setStatus(Status.KILL_UNACTIVITY);
		seckillItemInfo.setIsStart(Status.NO);
		seckillItemInfoMapper.add(seckillItemInfo);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//查询秒杀活动商品库存
	@Override
	public int querySeckillItemInventory(String itemId) {
		int inventory = Integer.parseInt(stringRedisTemplate.opsForValue().get(SECKILL_ITEM_INVENTORY+itemId));
		if(inventory < 0) {
			inventory = 0;
		}
		return inventory;
	}
	
	//激活秒杀活动
	@Override
	@Transactional
	public BaseResponse startUpSeckillActivity(String itemId) {
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		//查询秒杀商品详情
		SeckillItemInfo seckillItemInfo = seckillItemInfoMapper.findByItemId(itemId);
		if(seckillItemInfo == null) {
			return new BaseResponse(SeckillCode.SECKILL_ITEMINFO_NO_EXISTS,SeckillExceptionMsg.SECKILL_ITEMINFO_NO_EXISTS);
		}
		
		if(!seckillItemInfo.getStatus().equals(Status.KILL_UNACTIVITY)) {
			return new BaseResponse(SeckillCode.SECKILL_ACTIVITY_OR_END,SeckillExceptionMsg.SECKILL_ACTIVITY_OR_END);
		}
		
		//检查是否已经超时激活
		Date currentTime = new Date();
		Date startTime = seckillItemInfo.getStartTime();
		if(currentTime.after(startTime)) {
			return new BaseResponse(SeckillCode.SECKILL_STARTTIME_TIMEOUT,SeckillExceptionMsg.SECKILL_STARTTIME_TIMEOUT);
		}
		
		//更新激活状态
		seckillItemInfoMapper.updateStatus(Status.KILL_ACTIVITY, itemId);
		
		//将商品信息保存到redis
		stringRedisTemplate.opsForValue().set(SECKILL_ITEMINFO+itemId, JSON.toJSONString(seckillItemInfo));
		stringRedisTemplate.opsForValue().set(SECKILL_ITEM_INVENTORY+itemId, seckillItemInfo.getInventory()+"");
		
		//开启定时任务
		ThreadPoolBuilder.build(10, 30, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), "SeckillStartUpJob")
		.execute(new SeckillStartUpJob(itemId));
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	public SeckillStatus queryKillResult(String itemId, String userId) {
		if(StringUtils.isEmpty(itemId)) {
			return null;
		}
		SeckillStatus seckillStatus = (SeckillStatus) stringRedisTemplate.opsForHash().get(SECKILL_STATUS+userId, itemId);
		return seckillStatus;
	}

	@Override
	public List<SeckillOrderMessage> querySeckillOrders(String userId, String status) {
		List<SeckillOrderMessage> list = new ArrayList<SeckillOrderMessage>();
		Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(SECKILL_USER_ITEM_ORDERS+userId);
		if(entries == null) {
			return null;
		}
		for(Iterator iterator = entries.entrySet().iterator();iterator.hasNext();) {
			String key = (String) iterator.next();
			SeckillOrderMessage seckillOrderMessage = (SeckillOrderMessage) entries.get(key);
			String orderStatus = seckillOrderMessage.getStatus();
			if(orderStatus.equals(status)) {
				list.add(seckillOrderMessage);
			}
		}
		return list;
	}

	@Override
	public SeckillOrderMessage querySeckillOrderByOrderId(String orderId) {
		if(StringUtils.isEmpty(orderId)) {
			return null;
		}
		SeckillOrderMessage seckillOrderMessage = (SeckillOrderMessage) stringRedisTemplate.opsForHash().get(SECKILL_ORDER_LIST, orderId);
		return seckillOrderMessage;
	}

	@Override
	public SeckillItemInfo querySeckillItemInfoByItemId(String itemId) {
		if(StringUtils.isEmpty(itemId)) {
			return null;
		}
		String string = stringRedisTemplate.opsForValue().get(SECKILL_ITEMINFO+itemId);
		if(StringUtils.isEmpty(string)) {
			return null;
		}
		
		return JSON.parseObject(string, SeckillItemInfo.class);
	}
	
}
