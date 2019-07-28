package com.orangelala.framework.api;

import java.util.List;

import com.orangelala.framework.common.shop.request.ApplayOpenShopRequest;
import com.orangelala.framework.common.shop.request.QueryShopsRequest;
import com.orangelala.framework.common.shop.response.AggreeOpenShopResponse;
import com.orangelala.framework.common.shop.response.ApplayOpenShopResponse;
import com.orangelala.framework.model.shop.Shop;

/**
 * 店铺管理
 * @author chrilwe
 *
 */
public interface IShopController {
	//申请开店(平台管理)
	public ApplayOpenShopResponse applayOpenShop(ApplayOpenShopRequest OpenShopRequest);
	//平台管理同意用户开店(平台管理)
	public AggreeOpenShopResponse aggreeOpenShop(String userId);
	//查询店铺信息(商户和平台可以查询)
	public Shop queryShopInfo(String shopId);
	//查询商户列表(平台管理)
	public List<Shop> queryShops(QueryShopsRequest queryShopsRequest); 
}
