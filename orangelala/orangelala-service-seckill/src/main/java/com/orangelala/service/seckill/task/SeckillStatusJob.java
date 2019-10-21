package com.orangelala.service.seckill.task;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.orangelala.framework.model.seckill.SeckillStatus;
import com.orangelala.service.seckill.configs.Status;

/**
 * 抢购超时任务
 * @author chrilwe
 *
 */
public class SeckillStatusJob implements Runnable {
	
	private static final String SECKILL_STATUS = "seckill_status_";
	private static final String SECKILL_ORDER_LIST = "seckill_order_list";
	
	private String userId;
	private String itemId;
	
	public SeckillStatusJob(String userId, String itemId) {
		this.userId = userId;
		this.itemId = itemId;
	}
	
	@Override
	public void run() {
		//从spring单例池中取出所需单例对象
		StringRedisTemplate stringRedisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
		String[] aliases = SpringBeanUtil.getAliases("seckill.timeout");
		long timeout = Long.parseLong(aliases[0]);//秒
		
		//线程等待
		try {
			Thread.sleep(timeout*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//查询抢购状态，判断是否在超时时间内已经完成抢购,没有完成，判定抢购失败
		SeckillStatus seckillStatus = (SeckillStatus) stringRedisTemplate.opsForHash().get(SECKILL_STATUS+userId, itemId);
		if(seckillStatus.equals(Status.KILL_SUCCESS)) {
			return;
		} else if(seckillStatus.equals(Status.KILL_FAIL)) {
			//清空订单信息
		} else {
			//抢购超时
			seckillStatus.setStatus(Status.KILL_FAIL);
			stringRedisTemplate.opsForHash().put(SECKILL_STATUS+userId, itemId, seckillStatus);
		}
	}

}
