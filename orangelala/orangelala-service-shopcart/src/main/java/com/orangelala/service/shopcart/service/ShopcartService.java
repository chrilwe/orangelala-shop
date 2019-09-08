package com.orangelala.service.shopcart.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.shopcart.request.ShopcartAddRequest;
import com.orangelala.framework.model.item.ItemInfo;
/**
 * 购物车
 * @author chrilwe
 *
 */
public interface ShopcartService {
	// 添加商品到我的购物车
	public BaseResponse addItem2Cart(HttpServletRequest request,ShopcartAddRequest addRequest);

	// 根据商品id删除购物车中的商品
	public BaseResponse delItemFromCartById(HttpServletRequest request,String itemId);

	// 删除购物车中所有的商品
	public BaseResponse delAllItemFromCart(HttpServletRequest request);

	// 购物车商品列表
	public List<ItemInfo> itemList(HttpServletRequest request);
	
	//清空指定店铺的购物车
	public BaseResponse delAllShopItemFromCart(HttpServletRequest request, String shopId);
	
	//店铺购物车列表
	public List<ItemInfo> shopItemList(HttpServletRequest request, String shopId);
	
}
