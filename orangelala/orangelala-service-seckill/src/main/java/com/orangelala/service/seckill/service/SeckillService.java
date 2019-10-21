package com.orangelala.service.seckill.service;

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.item.Item;
import com.orangelala.framework.model.seckill.SeckillItemInfo;
import com.orangelala.framework.model.seckill.SeckillOrderMessage;
import com.orangelala.framework.model.seckill.SeckillStatus;

public interface SeckillService {
	//开始抢购
	public BaseResponse seckill(String itemId, String userId);
	
	//创建抢购活动
	public BaseResponse createSeckillActivity(SeckillItemInfo seckillItemInfo);
	
	//查询秒杀商品库存
	public int querySeckillItemInventory(String itemId);
	
	//激活秒杀活动
	public BaseResponse startUpSeckillActivity(String itemId);
	
	//查询抢购结果
	public SeckillStatus queryKillResult(String itemId, String userId);
	
	//查询秒杀清单
	public List<SeckillOrderMessage> querySeckillOrders(String userId, String status);
	
	public SeckillOrderMessage querySeckillOrderByOrderId(String orderId);
	
	//查询秒杀商品信息
	public SeckillItemInfo querySeckillItemInfoByItemId(String itemId);
}
