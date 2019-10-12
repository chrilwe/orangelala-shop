package com.orangelala.service.seckill.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.item.response.msg.ItemMsg;
import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.model.seckill.SeckillDetail;
import com.orangelala.framework.model.seckill.SeckillOrderMessage;
import com.orangelala.framework.model.seckill.SeckillStatus;
import com.orangelala.service.seckill.configs.Status;
import com.orangelala.service.seckill.execption.SeckillException;
import com.orangelala.service.seckill.execption.msg.SeckillExceptionMsg;
import com.orangelala.service.seckill.service.MultiCreateOrderService;
import com.orangelala.service.seckill.service.SeckillService;
import com.orangelala.service.seckill.task.SeckillStatusJob;
import com.orangelala.service.seckill.threadpool.MultiThreadPool;
import com.orangelala.service.seckill.threadpool.SeckillStatusJobThreadPool;

@Service
public class SeckillServiceImpl implements SeckillService {
	
	private static final String SECKILL_TIMES = "seckill_times_";
	private static final String SECKILL_DETAIL_QUEUE = "seckill_detail_queue";
	private static final String SECKILL_STATUS = "seckill_status_";
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private MultiCreateOrderService multiCreateOrderService;

	@Override
	public BaseResponse seckill(String itemId, String userId) {
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(Code.PARAM_NULL,ItemMsg.ITEM_ID_NULL);
		}
		
		//一个用户只能抢购一次，不能重复抢购
		Long increment = stringRedisTemplate.opsForValue().increment(SECKILL_TIMES+userId, 1l);
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
		stringRedisTemplate.opsForList().leftPush(SECKILL_DETAIL_QUEUE, JSON.toJSONString(seckillDetail));
		
		//设置抢购状态标识 : 排队中
		SeckillStatus seckillStatus = new SeckillStatus();
		seckillStatus.setCreateTime(currentTime);
		seckillStatus.setItemID(itemId);
		seckillStatus.setUserID(userId);
		seckillStatus.setStatus(Status.WAITING_IN_QUEUE);
		stringRedisTemplate.opsForHash().put(SECKILL_STATUS+userId, itemId, seckillStatus);
		
		//异步进行多线程下单
		MultiThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				multiCreateOrderService.createOrder();
			}			
		});
		
		//开启超时抢购检测线程
		SeckillStatusJobThreadPool.getInstance().execute(new SeckillStatusJob(userId,itemId));
		return null;
	}
	
}
