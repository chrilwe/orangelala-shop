package com.orangelala.service.item.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.price.request.UpdateItemPriceRequest;
import com.orangelala.framework.common.item.response.code.ItemCode;
import com.orangelala.framework.common.item.response.msg.ItemMsg;
import com.orangelala.framework.model.item.Item;
import com.orangelala.framework.model.item.details.ItemDetails;
import com.orangelala.framework.model.item.inventory.ItemInventory;
import com.orangelala.framework.model.item.params.ItemParams;
import com.orangelala.framework.model.item.price.ItemPrice;
import com.orangelala.framework.model.item.status.ItemStatus;
import com.orangelala.framework.utils.GenerateNum;
import com.orangelala.service.item.client.ItemInventoryClient;
import com.orangelala.service.item.client.ItemPriceClient;
import com.orangelala.service.item.mapper.ItemDetailsMapper;
import com.orangelala.service.item.mapper.ItemMapper;
import com.orangelala.service.item.mapper.ItemParamsMapper;

@Service
public class ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemParamsMapper itemParamsMapper;
	@Autowired
	private ItemDetailsMapper itemDetailsMapper;
	@Autowired
	private ItemInventoryClient itemInventoryClient;
	@Autowired
	private ItemPriceClient itemPriceClient;
	
	//添加商品基本信息
	@Transactional
	public BaseResponse addItem(Item item) {
		if(item == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		String shopId = item.getShopId();
		if(StringUtils.isEmpty(shopId)) {
			return new BaseResponse(ItemCode.UNKNOWN_SHOP,ItemMsg.UNKNOWN_SHOP);
		}
		//生成商品id
		GenerateNum num = new GenerateNum();
		String itemId = num.generate();
		item.setId(itemId);
		item.setCreated(new Date());
		item.setStatus(ItemStatus.NORMAL);
		itemMapper.add(item);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//添加商品参数信息
	@Transactional
	public BaseResponse addItemParams(ItemParams itemParams) {
		if(itemParams == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		String itemId = itemParams.getItemId();
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(ItemCode.ITEM_ID_NULL,ItemMsg.ITEM_ID_NULL);
		}
		Item findById = itemMapper.findById(itemId);
		if(findById == null) {
			return new BaseResponse(ItemCode.ITEM_NO_EXISTS,ItemMsg.ITEM_NO_EXISTS);
		}
		GenerateNum num = new GenerateNum();
		String id = num.generate();
		itemParams.setId(id);
		itemParamsMapper.add(itemParams);
		return new BaseResponse(ItemCode.SUCCESS,ItemMsg.SUCCESS);
	}
	
	//添加商品详情
	@Transactional
	public BaseResponse addItemDetails(ItemDetails itemDetails) {
		if(itemDetails == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		String itemId = itemDetails.getItemId();
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(ItemCode.ITEM_ID_NULL,ItemMsg.ITEM_ID_NULL);
		}
		Item findById = itemMapper.findById(itemId);
		if(findById == null) {
			return new BaseResponse(ItemCode.ITEM_NO_EXISTS,ItemMsg.ITEM_NO_EXISTS);
		}
		GenerateNum num = new GenerateNum();
		String id = num.generate();
		itemDetails.setId(id);
		itemDetailsMapper.add(itemDetails);
		return new BaseResponse(ItemCode.SUCCESS,ItemMsg.SUCCESS);
	}
	
	//添加商品库存
	public BaseResponse addItemInventory(String itemId,int num) {
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(ItemCode.ITEM_ID_NULL,ItemMsg.ITEM_ID_NULL);
		}
		//查询商品是否存在
		Item item = itemMapper.findById(itemId);
		if(item == null) {
			return new BaseResponse(ItemCode.ITEM_NO_EXISTS,ItemMsg.ITEM_NO_EXISTS);
		}
		ItemInventory itemInventory = new ItemInventory();
		itemInventory.setId(itemId);
		itemInventory.setItemId(itemId);
		itemInventory.setNum(num);
		itemInventory.setUpdateTime(new Date());
		return itemInventoryClient.addItemInventory(itemInventory);	
	}
	
	//添加商品价格
	public BaseResponse addItemPrice(String itemId,long price) {
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(ItemCode.ITEM_ID_NULL,ItemMsg.ITEM_ID_NULL);
		}
		//查询商品是否存在
		Item item = itemMapper.findById(itemId);
		if(item == null) {
			return new BaseResponse(ItemCode.ITEM_NO_EXISTS,ItemMsg.ITEM_NO_EXISTS);
		}
		UpdateItemPriceRequest request = new UpdateItemPriceRequest();
		request.setItemId(itemId);
		request.setPrice(price);
		return itemPriceClient.updateItemPrice(request);
	}
}
