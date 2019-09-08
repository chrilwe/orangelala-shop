package com.orangelala.service.shopcart.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.auth.response.code.AuthCode;
import com.orangelala.framework.common.auth.response.msg.AuthMsg;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.item.inventory.response.code.InventoryCode;
import com.orangelala.framework.common.item.inventory.response.msg.InventoryMsg;
import com.orangelala.framework.common.item.response.code.ItemCode;
import com.orangelala.framework.common.item.response.msg.ItemMsg;
import com.orangelala.framework.common.shopcart.request.ShopcartAddRequest;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.ucenter.User;
import com.orangelala.framework.utils.Oauth2Util;
import com.orangelala.service.shopcart.client.InventoryClient;
import com.orangelala.service.shopcart.client.ItemClient;
import com.orangelala.service.shopcart.service.ShopcartService;

@Service
public class ShopcartServiceImpl implements ShopcartService {
	private static final String shopcartKey = "shopcart_";
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ItemClient itemClient;
	@Autowired
	private InventoryClient inventoryClient;

	@Override
	public BaseResponse addItem2Cart(HttpServletRequest request,ShopcartAddRequest addRequest) {
		User userInfo = this.getUserInfo(request);
		if(userInfo == null) {
			return new BaseResponse(AuthCode.UNAUTHORIZED,AuthMsg.UNAUTHORIZED);
		}
		String userId = userInfo.getId();
		if(addRequest == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		String itemId = addRequest.getItemId();
		int buyNum = addRequest.getNum();
		
		//查询库存
		QueryItemNumRequest queryInventoryRequest = new QueryItemNumRequest();
		queryInventoryRequest.setItemId(itemId);
		QueryItemNumResponse queryItemNum = inventoryClient.queryItemNum(queryInventoryRequest);
		int currentInventory = queryItemNum.getNum();
		if(currentInventory < buyNum) {
			return new BaseResponse(InventoryCode.ITEM_INVENTORY_NO_ENOUGH,InventoryMsg.ITEM_INVENTORY_NO_ENOUGH);
		}
		
		//查询redis中是否有此商品，如果有数量加1，否则添加数据
		//存放数据结构{shopcart_"userId","itemId","value"}
		String value = (String) stringRedisTemplate.opsForHash().get(shopcartKey+userId, itemId);
		if(StringUtils.isEmpty(value)) {
			ItemInfo itemInfo = itemClient.findItemInfoById(itemId);
			if(itemInfo == null) {
				return new BaseResponse(ItemCode.ITEM_NO_EXISTS,ItemMsg.ITEM_NO_EXISTS);
			}
			stringRedisTemplate.opsForHash().put(shopcartKey+userId, itemId, JSON.toJSONString(itemInfo));
		} else {
			ItemInfo itemInfo = JSON.parseObject(value, ItemInfo.class);
			int num = itemInfo.getNum();
			if((num+buyNum) > currentInventory) {
				return new BaseResponse(InventoryCode.ITEM_INVENTORY_NO_ENOUGH,InventoryMsg.ITEM_INVENTORY_NO_ENOUGH);
			}
			itemInfo.setNum(num+buyNum);
			stringRedisTemplate.opsForHash().put(shopcartKey+userId, itemId, JSON.toJSONString(itemInfo));
		}
		return null;
	}

	@Override
	public BaseResponse delItemFromCartById(HttpServletRequest request,String itemId) {
		User userInfo = this.getUserInfo(request);
		if(userInfo == null) {
			return new BaseResponse(AuthCode.UNAUTHORIZED,AuthMsg.UNAUTHORIZED);
		}
		String userId = userInfo.getId();
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(ItemCode.ITEM_ID_NULL,ItemMsg.ITEM_ID_NULL);
		}
		Long delete = stringRedisTemplate.opsForHash().delete(shopcartKey+userId, itemId);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	public BaseResponse delAllItemFromCart(HttpServletRequest request) {
		User userInfo = this.getUserInfo(request);
		if(userInfo == null) {
			return new BaseResponse(AuthCode.UNAUTHORIZED,AuthMsg.UNAUTHORIZED);
		}
		String userId = userInfo.getId();
		stringRedisTemplate.opsForHash().delete(shopcartKey+userId);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	public List<ItemInfo> itemList(HttpServletRequest request) {
		User userInfo = this.getUserInfo(request);
		if(userInfo == null) {
			throw new RuntimeException(AuthMsg.UNAUTHORIZED);
		}
		String userId = userInfo.getId();
		List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
		Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(shopcartKey+userId);
		if(entries == null) {
			return null;
		}
		for(Iterator iterator = entries.keySet().iterator();iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) entries.get(key);
			itemInfos.add(JSON.parseObject(value, ItemInfo.class));
		}
		return itemInfos;
	}

	private User getUserInfo(HttpServletRequest request) {
		Map<String, String> userInfo = Oauth2Util.getJwtClaimsFromHeader(request);
		if(userInfo == null) {
			return null;
		}
		String userId = userInfo.get("userId");
		String username = userInfo.get("username");
		String userPic = userInfo.get("userPic");
		String utype = userInfo.get("utype");
		if(StringUtils.isEmpty(userId)) {
			return null;
		}
		User user = new User();
		user.setUsername(username);
		user.setId(userId);
		user.setUserpic(userPic);
		user.setUtype(utype);
		return user;
	}

	@Override
	public BaseResponse delAllShopItemFromCart(HttpServletRequest request, String shopId) {
		User userInfo = this.getUserInfo(request);
		if(userInfo == null) {
			throw new RuntimeException(AuthMsg.UNAUTHORIZED);
		}
		//查询当前用户所有的购物车商品
		List<ItemInfo> itemList = this.itemList(request);
		
		//从中帅选出当前店铺的购物车商品，移除
		for(ItemInfo itemInfo : itemList) {
			String sId = itemInfo.getShopId();
			if(sId.equals(shopId)) {
				String itemId = itemInfo.getId();
				stringRedisTemplate.opsForHash().delete(shopcartKey+userInfo.getId(), itemId);
			}
		}
		
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	public List<ItemInfo> shopItemList(HttpServletRequest request, String shopId) {
		User userInfo = this.getUserInfo(request);
		if(userInfo == null) {
			throw new RuntimeException(AuthMsg.UNAUTHORIZED);
		}
		//查询当前用户所有购物车商品
		List<ItemInfo> itemList = this.itemList(request);
		
		//帅选出当前店铺的购物车商品信息
		for(ItemInfo itemInfo : itemList) {
			String sId = itemInfo.getShopId();
			if(!shopId.equals(sId)) {
				itemList.remove(itemInfo);
			}
		}
		return itemList;
	}

}
