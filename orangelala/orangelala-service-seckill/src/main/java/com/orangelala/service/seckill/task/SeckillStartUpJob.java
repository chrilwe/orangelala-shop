package com.orangelala.service.seckill.task;

import java.util.Date;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.model.seckill.SeckillItemInfo;
import com.orangelala.service.seckill.configs.Status;

/**
 * 活动开启定时器
 * @author chrilwe
 *
 */
public class SeckillStartUpJob implements Runnable{
	
	private String itemId;
	private static final String SECKILL_ITEMINFO = "seckill_iteminfo_";
	
	public SeckillStartUpJob(String ItemId) {
		this.itemId = itemId;
	}
	
	@Override
	public void run() {
		//当前时间
		long currentTime = System.currentTimeMillis();
		
		//活动开启时间
		StringRedisTemplate stringRedisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
		String string = stringRedisTemplate.opsForValue().get(SECKILL_ITEMINFO+itemId);
		SeckillItemInfo seckillItemInfo = JSON.parseObject(string, SeckillItemInfo.class);
		Date startTime = seckillItemInfo.getStartTime();
		Date endTime = seckillItemInfo.getEndTime();
		long start_time = startTime.getTime();
		long end_time = endTime.getTime();
		
		//定时开启和结束
		if((currentTime <= start_time) && (end_time > start_time)) {
			try {
				//开始
				Thread.sleep(start_time - currentTime);
				seckillItemInfo.setIsStart(Status.YES);
				seckillItemInfo.setStatus(Status.KILL_START);
				stringRedisTemplate.opsForValue().set(SECKILL_ITEMINFO+itemId, JSON.toJSONString(seckillItemInfo));
				
				//结束
				long currentTime1 = System.currentTimeMillis();
				Thread.sleep(end_time - currentTime1);
				seckillItemInfo.setIsStart(Status.NO);
				seckillItemInfo.setStatus(Status.KILL_END);
				stringRedisTemplate.opsForValue().set(SECKILL_ITEMINFO+itemId, JSON.toJSONString(seckillItemInfo));
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
