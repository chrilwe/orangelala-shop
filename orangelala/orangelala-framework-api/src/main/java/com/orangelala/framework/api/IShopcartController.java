package com.orangelala.framework.api;
/**
 * 购物车管理
 * @author chrilwe
 *
 */

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.shopcart.request.ShopcartAddRequest;
import com.orangelala.framework.model.item.ItemInfo;

public interface IShopcartController {
	//添加商品到我的购物车
	public BaseResponse addItem2Cart(ShopcartAddRequest request);
	//根据商品id删除购物车中的商品
	public BaseResponse delItemFromCartById(String itemId);
	//删除购物车中所有的商品
	public BaseResponse delAllItemFromCart();
	//购物车商品列表
	public List<ItemInfo> itemList();
	//清空指定店铺的购物车
	public BaseResponse delAllShopItemFromCart(String shopId);
	//店铺购物车列表
	public List<ItemInfo> shopItemList(String shopId);
}
