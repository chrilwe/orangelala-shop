package com.orangelala.service.price.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.price.request.QueryItemPriceRequest;
import com.orangelala.framework.common.item.price.request.UpdateItemPriceRequest;
import com.orangelala.framework.common.item.price.response.QueryItemPriceResponse;
import com.orangelala.framework.common.item.response.code.ItemCode;
import com.orangelala.framework.common.item.response.msg.ItemMsg;
import com.orangelala.framework.model.item.price.ItemPrice;
import com.orangelala.service.price.mapper.ItemPriceMapper;

@Service
public class ItemPriceService {
	
	@Autowired
	private ItemPriceMapper itemPriceMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	// 查询商品价格
	public QueryItemPriceResponse queryItemPrice(QueryItemPriceRequest request) {
		if(request == null) {
			return new QueryItemPriceResponse(Code.PARAM_NULL,Msg.PARAM_NULL,"",0);
		}
		String itemId = request.getItemId();
		long price = 0;
		//先从redis中查询
		String itemPriceStr = stringRedisTemplate.boundValueOps("itemPrice_"+itemId).get();
		//redis中没有查到，从数据库中查询
		if(StringUtils.isEmpty(itemPriceStr)) {
			ItemPrice itemPrice = itemPriceMapper.findByItemId(itemId);
			if(itemPrice == null) {
				return new QueryItemPriceResponse(ItemCode.ITEM_NO_EXISTS,ItemMsg.ITEM_NO_EXISTS,itemId,0);
			}
			price = itemPrice.getPrice();
		} else {
			price = Integer.parseInt(itemPriceStr);
		}
		return new QueryItemPriceResponse(Code.SUCCESS,Msg.SUCCESS,itemId,price);
	}

	// 更新商品价格
	@Transactional
	public BaseResponse updateItemPrice(UpdateItemPriceRequest request) {
		if(request == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		String itemId = request.getItemId();
		long price = request.getPrice();
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(ItemCode.ITEM_ID_NULL,ItemMsg.ITEM_ID_NULL);
		}
		if(price <= 0) {
			return new BaseResponse(ItemCode.ITEM_PRICE_MUST_BIGGER_0,ItemMsg.ITEM_PRICE_MUST_BIGGER_0);
		}
		ItemPrice itemPrice = itemPriceMapper.findByItemId(itemId);
		if(itemPrice != null) {
			int version = itemPrice.getVersion();
			itemPrice.setUpdateTime(new Date());
			itemPrice.setOriginPrice(itemPrice.getPrice());
			itemPrice.setPrice(price);
			int update = itemPriceMapper.update(itemPrice);
			if(update <= 0) {
				return new BaseResponse(ItemCode.ITEM_PRICE_UPDATING,ItemMsg.ITEM_PRICE_UPDATING);
			}
		} else {
			return new BaseResponse(ItemCode.ITEM_NO_EXISTS,ItemMsg.ITEM_NO_EXISTS);
		}
		return new BaseResponse(ItemCode.SUCCESS,ItemMsg.SUCCESS);
	}

	// 添加商品价格
	@Transactional
	public BaseResponse addItemPrice(ItemPrice itemPrice) {
		if(itemPrice == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		long price = itemPrice.getPrice();
		String itemId = itemPrice.getItemId();
		if(price <= 0) {
			return new BaseResponse(ItemCode.ITEM_PRICE_MUST_BIGGER_0,ItemMsg.ITEM_PRICE_MUST_BIGGER_0);
		}
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(ItemCode.ITEM_ID_NULL,ItemMsg.ITEM_ID_NULL);
		}
		itemPriceMapper.add(itemPrice);
		stringRedisTemplate.boundValueOps("itemPrice_"+itemId).set(price+"");
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
}
