package com.orangelala.service.inventory.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.item.inventory.response.code.InventoryCode;
import com.orangelala.framework.common.item.inventory.response.msg.InventoryMsg;
import com.orangelala.framework.model.item.inventory.ItemInventory;
import com.orangelala.framework.utils.ZkUtil;
import com.orangelala.service.inventory.mapper.ItemInventoryMapper;

@Service
public class ItemInventoryService {
	
	@Autowired
	private ItemInventoryMapper itemInventoryMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ZkUtil zkUtil;
	
	@Value("${inventory.zkConnectTimeout}")
	private long zkConnectTimeout;
	
	//查询商品库存
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request) {
		if(request == null) {
			return new QueryItemNumResponse(Code.PARAM_NULL,Msg.PARAM_NULL,"",0);
		}
		String itemId = request.getItemId();
		//从redis中查询库存
		String itemNumStr = stringRedisTemplate.boundValueOps("itemNum_"+itemId).get();
		int itemNum = 0;
		if(StringUtils.isEmpty(itemNumStr)) {
			//redis没有获取到库存，从数据库中查询
			ItemInventory itemInventory = itemInventoryMapper.findByItemId(itemId);
			itemNum = itemInventory.getNum();
		} else {
			itemNum = Integer.parseInt(itemNumStr);
		}
		return new QueryItemNumResponse(Code.SUCCESS,Msg.SUCCESS,itemId,itemNum);
	}
	
	
	//更新商品库存
	@Transactional
	public BaseResponse updateItemNumResponse(UpdateItemNumRequest request) {
		if(request == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		int buyItemNum = request.getBuyItemNum();
		String itemId = request.getItemId();
		if(buyItemNum <= 0) {
			return new BaseResponse(InventoryCode.ITEM_NUM_NULL,InventoryMsg.ITEM_NUM_NULL);
		}
		//多线程下，对更新库存添加分布式锁
		boolean createNode = zkUtil.createNode("/"+itemId, "", zkConnectTimeout);
		if(createNode) {
			return new BaseResponse(InventoryCode.SYSTEM_BUSY,InventoryMsg.SYSTEM_BUSY);
		}
		//查询库存
		ItemInventory itemNum = itemInventoryMapper.findByItemId(itemId);
		int num = itemNum.getNum();
		if(num < buyItemNum) {
			return new BaseResponse(InventoryCode.ITEM_INVENTORY_NO_ENOUGH,InventoryMsg.ITEM_INVENTORY_NO_ENOUGH);
		}
		num = num - buyItemNum;
		ItemInventory itemInventory = new ItemInventory();
		itemInventory.setUpdateTime(new Date());
		itemInventory.setNum(num);
		itemInventoryMapper.update(itemInventory);
		stringRedisTemplate.boundValueOps("itemNum_"+itemId).set(num+"");
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	
	//添加商品库存
	@Transactional
	public BaseResponse addItemInventory(ItemInventory itemInventory) {
		if(itemInventory == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		itemInventoryMapper.add(itemInventory);
		stringRedisTemplate.boundValueOps("itemNum_"+itemInventory.getItemId()).set(itemInventory.getNum()+"");
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
}
