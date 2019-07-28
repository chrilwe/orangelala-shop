package com.orangelala.framework.api;
/**
 * 商品价格服务
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.price.request.QueryItemPriceRequest;
import com.orangelala.framework.common.item.price.request.UpdateItemPriceRequest;
import com.orangelala.framework.common.item.price.response.QueryItemPriceResponse;
import com.orangelala.framework.model.item.price.ItemPrice;

public interface IPriceController {
	//查询商品价格
	public QueryItemPriceResponse queryItemPrice(QueryItemPriceRequest request);
	//更新商品价格
	public BaseResponse updateItemPrice(UpdateItemPriceRequest request);
	//添加商品价格
	public BaseResponse addItemPrice(ItemPrice itemPrice);
}
