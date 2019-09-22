package com.orangelala.framework.api;
/**
 * 商品库存服务
 * @author chrilwe
 *
 */

import java.util.List;
import java.util.Map;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.item.inventory.response.ReduceInventoryResponse;


public interface IInventoryController {
	// 查询库存
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request);

	//减少库存
	public ReduceInventoryResponse reduce(Map<String,Integer> map);
	
	//添加库存
	public BaseResponse increase(String itemId,int num);
}
