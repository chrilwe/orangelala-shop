package com.orangelala.framework.api;
/**
 * 商品库存服务
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.model.item.inventory.ItemInventory;

public interface IInventoryController {
	//查询库存
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request);
	//更新库存
	public BaseResponse updateItemNumResponse(UpdateItemNumRequest request);
	//添加商品库存
	public BaseResponse addItemInventory(ItemInventory itemInventory);
}
