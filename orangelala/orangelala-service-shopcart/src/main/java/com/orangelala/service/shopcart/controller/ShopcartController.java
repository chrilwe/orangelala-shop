package com.orangelala.service.shopcart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IShopcartController;
import com.orangelala.framework.common.base.BaseController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.shopcart.request.ShopcartAddRequest;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.service.shopcart.service.ShopcartService;

@RestController
@RequestMapping("/shopcart")
public class ShopcartController extends BaseController implements IShopcartController {
	
	@Autowired
	private ShopcartService shopcartService;
	
	//添加商品到购物车
	@Override
	@GetMapping("/add")
	public BaseResponse addItem2Cart(ShopcartAddRequest addRequest) {
		BaseResponse response = null;
		try {
			response = shopcartService.addItem2Cart(request, addRequest);
		} catch (Exception e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return response;
	}
	
	//从购物车中根据商品id删除
	@Override
	@GetMapping("/deleteById")
	public BaseResponse delItemFromCartById(String itemId) {
		BaseResponse response = null;
		try {
			response = shopcartService.delItemFromCartById(request, itemId);
		} catch (Exception e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return response;
	}
	
	//清空购物车
	@Override
	@GetMapping("/deleteAll")
	public BaseResponse delAllItemFromCart() {
		BaseResponse response = null;
		try {
			response = shopcartService.delAllItemFromCart(request);
		} catch (Exception e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return response;
	}
	
	//购物车商品列表
	@Override
	@GetMapping("/itemlist")
	public List<ItemInfo> itemList() {
		List<ItemInfo> itemList = null;
		try {
			itemList = shopcartService.itemList(request);
		} catch (Exception e) {
			return null;
		}
		return itemList;
	}
	
	//清空当前店铺的购物车商品
	@Override
	@GetMapping("/deleteAllShopItem")
	public BaseResponse delAllShopItemFromCart(String shopId) {
		
		return shopcartService.delAllShopItemFromCart(request, shopId);
	}
	
	//当前商铺的购物车商品列表
	@Override
	@GetMapping("/shopItemList")
	public List<ItemInfo> shopItemList(String shopId) {
		
		return shopcartService.shopItemList(request, shopId);
	}
	
}
