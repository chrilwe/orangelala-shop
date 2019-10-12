package com.orangelala.service.seckill.service;

import com.orangelala.framework.common.base.BaseResponse;

public interface SeckillService {
	//开始抢购
	public BaseResponse seckill(String itemId, String userId);
}
