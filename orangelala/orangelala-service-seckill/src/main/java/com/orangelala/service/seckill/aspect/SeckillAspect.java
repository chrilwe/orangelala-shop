package com.orangelala.service.seckill.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.model.seckill.SeckillItemInfo;
import com.orangelala.service.seckill.configs.Status;
import com.orangelala.service.seckill.execption.SeckillException;
import com.orangelala.service.seckill.execption.msg.SeckillExceptionMsg;
import com.orangelala.service.seckill.service.SeckillService;

@Aspect
@Component
public class SeckillAspect implements Ordered {
	
	private static final String SECKILL_ITEMINFO = "seckill_iteminfo_";
	private static final String SECKILL_ITEM_INVENTORY = "seckil_item_inventory_";
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private SeckillService seckillService;

	@Override
	public int getOrder() {

		return 0;
	}
	
	@Pointcut("@Annotation(com.orangelala.service.seckill.annotation.CheckSeckillStart) && args(itemId,userId)")
	public void seckillPointCut(String itemId,String userId){}
	
	@Around("seckillPointCut(itemId, userId)")
	public void around(ProceedingJoinPoint joinPoint, String itemId,String userId) throws Throwable {
		this.check(itemId);
		Object proceed = joinPoint.proceed();
	}
	
	//检查活动是否开启,库存是否充足
	private void check(String itemId) {
		String string = stringRedisTemplate.opsForValue().get(SECKILL_ITEMINFO+itemId);
		if(StringUtils.isEmpty(string)) {
			throw new SeckillException(SeckillExceptionMsg.SECKILL_ITEMINFO_NO_EXISTS);
		}
		
		SeckillItemInfo seckillItemInfo = JSON.parseObject(string, SeckillItemInfo.class);
		String status = seckillItemInfo.getStatus();
		if(status.equals(Status.KILL_END)) {
			throw new SeckillException(SeckillExceptionMsg.SECKILL_END);
		} else if(status.equals(Status.KILL_START)) {
			//活动开始，检查库存是否充足
			int inventory = seckillService.querySeckillItemInventory(itemId);
			if(inventory <= 0) {
				throw new SeckillException(SeckillExceptionMsg.SECKILL_END);
			}
		} else {
			throw new SeckillException(SeckillExceptionMsg.SECKILL_WAITING_BEGIN);
		}
	}
}
