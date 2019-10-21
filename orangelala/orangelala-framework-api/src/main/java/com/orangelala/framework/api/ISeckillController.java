package com.orangelala.framework.api;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.seckill.SeckillStatus;

/**
 * 秒杀
 * @author chrilwe
 *
 */
public interface ISeckillController {
	public BaseResponse seckill(String itemId);
	
	public SeckillStatus seckillStatus(String itemId);
	
	public int queryInventory(String itemId);
}
