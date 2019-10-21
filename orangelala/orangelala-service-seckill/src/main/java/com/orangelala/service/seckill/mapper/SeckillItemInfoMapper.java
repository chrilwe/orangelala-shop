package com.orangelala.service.seckill.mapper;

import com.orangelala.framework.model.seckill.SeckillItemInfo;

public interface SeckillItemInfoMapper {
	public void add(SeckillItemInfo seckillItemInfo);
	
	public void updateStatus(String status, String itemId);
	
	public void update(SeckillItemInfo seckillItemInfo);
	
	public SeckillItemInfo findByItemId(String itemId);
}
