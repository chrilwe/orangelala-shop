package com.orangelala.service.seckill.task;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.orangelala.framework.model.seckill.SeckillOrderMessage;
import com.orangelala.framework.utils.ZkUtil;
import com.orangelala.service.seckill.configs.Status;

/**
 * 支付超时监控
 * 
 * @author chrilwe
 *
 */
public class PayTimeMonitor implements Runnable {

	private static final String SECKILL_ORDER_LIST = "seckill_order_list";

	@Override
	public void run() {
		loopQueryPayStatusAndProcess();
	}

	private void loopQueryPayStatusAndProcess() {
		while(true) {
			// 取出订单列表数据，查询支付是否超时
			StringRedisTemplate stringRedisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
			ZkUtil zkUtil = SpringBeanUtil.getBean(ZkUtil.class);
			
			Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(SECKILL_ORDER_LIST);
			for (Iterator iterator = entries.entrySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				SeckillOrderMessage seckillOrderMessage = (SeckillOrderMessage) entries.get(key);
				
				
			}
		}
	}
	
	//处理超时订单，保证其幂等
	//TODO 优化秒杀订单的list，让它按照过期时间有序排序，减少遍历list时间
	private void processOrder(StringRedisTemplate stringRedisTemplate, SeckillOrderMessage seckillOrderMessage) {
		long currentTimeSec = System.currentTimeMillis()/1000;
		Date createTime = seckillOrderMessage.getCreateTime();//下单时间
		long createTimeSec = createTime.getTime()/1000;
		int payTimeLimitSec = seckillOrderMessage.getPayTimeLimitSec();//支付超时时间
		String status = seckillOrderMessage.getStatus();//订单状态
		
		//订单支付超时,自动取消订单,回滚库存
		if(status.equals(Status.PAY_NO)) {
			if((currentTimeSec - createTimeSec) > payTimeLimitSec) {
				seckillOrderMessage.setStatus(Status.PAY_CANCEL);
				stringRedisTemplate.opsForHash().put(SECKILL_ORDER_LIST, seckillOrderMessage.getOrderID(), seckillOrderMessage);
				
			}
		}
	}
}
