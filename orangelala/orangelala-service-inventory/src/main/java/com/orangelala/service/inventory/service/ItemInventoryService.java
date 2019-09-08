package com.orangelala.service.inventory.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.inventory.exception.ItemInventoryException;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.item.inventory.response.code.InventoryCode;
import com.orangelala.framework.common.item.inventory.response.msg.InventoryMsg;
import com.orangelala.framework.model.item.inventory.ItemInventory;
import com.orangelala.framework.model.item.inventory.ItemInventoryRecord;
import com.orangelala.framework.utils.ZkUtil;
import com.orangelala.service.inventory.mapper.ItemInventoryMapper;
import com.orangelala.service.inventory.mapper.ItemInventoryRecordMapper;

@Service
public class ItemInventoryService {

	@Autowired
	private ItemInventoryMapper itemInventoryMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ZkUtil zkUtil;
	@Autowired
	private ItemInventoryRecordMapper itemInventoryRecordMapper;

	@Value("${inventory.zkConnectTimeout}")
	private long zkConnectTimeout;

	// 查询商品库存
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request) {
		if (request == null) {
			return new QueryItemNumResponse(Code.PARAM_NULL, Msg.PARAM_NULL, "", 0);
		}
		String itemId = request.getItemId();
		// 从redis中查询库存
		String itemNumStr = stringRedisTemplate.boundValueOps("itemNum_" + itemId).get();
		int itemNum = 0;
		if (StringUtils.isEmpty(itemNumStr)) {
			// redis没有获取到库存，从数据库中查询
			ItemInventory itemInventory = itemInventoryMapper.findByItemId(itemId);
			itemNum = itemInventory.getNum();
		} else {
			itemNum = Integer.parseInt(itemNumStr);
		}
		return new QueryItemNumResponse(Code.SUCCESS, Msg.SUCCESS, itemId, itemNum);
	}

	// 更新商品库存
	@Transactional
	public BaseResponse updateItemNumResponse(UpdateItemNumRequest request) {
		if (request == null) {
			return new BaseResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		}
		int buyItemNum = request.getBuyItemNum();
		String itemId = request.getItemId();
		String orderNumber = request.getOrderNumber();
		String userId = request.getUserId();
		if (buyItemNum <= 0) {
			return new BaseResponse(InventoryCode.ITEM_NUM_NULL, InventoryMsg.ITEM_NUM_NULL);
		}
		return this.update(itemId, buyItemNum, orderNumber, userId);
	}

	private BaseResponse update(String itemId, int buyItemNum, String orderNumber, String userId) {
		// 多线程下，对更新库存添加分布式锁
		boolean createNode = zkUtil.createNode("/" + itemId, "", zkConnectTimeout);
		if (!createNode) {
			throw new ItemInventoryException(InventoryMsg.SYSTEM_BUSY);
		}
		// 查询库存,不能从redis中查询有可能当前数据库已经减少库存而redis还没有来得及更新导致数据不一致
		ItemInventory itemNum = itemInventoryMapper.findByItemId(itemId);
		int num = itemNum.getNum();
		if (num < buyItemNum) {
			throw new ItemInventoryException(InventoryMsg.ITEM_INVENTORY_NO_ENOUGH);
		}
		num = num - buyItemNum;
		ItemInventory itemInventory = new ItemInventory();
		itemInventory.setUpdateTime(new Date());
		itemInventory.setNum(num);
		itemInventoryMapper.update(itemInventory);
		ItemInventoryRecord itemInventoryRecord = new ItemInventoryRecord();
		itemInventoryRecord.setItemId(itemId);
		itemInventoryRecord.setNum(buyItemNum);
		itemInventoryRecord.setOrderNumber(orderNumber);
		itemInventoryRecord.setUpdateTime(new Date());
		itemInventoryRecordMapper.addItemInventoryRecord(itemInventoryRecord);
		stringRedisTemplate.boundValueOps("itemNum_" + itemId).set(num + "");
		zkUtil.deleteNode("/" + itemId);
		return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
	}

	// 添加商品库存
	@Transactional
	public BaseResponse addItemInventory(ItemInventory itemInventory) {
		if (itemInventory == null) {
			return new BaseResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		}
		ItemInventory findByItemId = itemInventoryMapper.findByItemId(itemInventory.getItemId());
		if(findByItemId != null) {
			int num = findByItemId.getNum();
			//商品库存没有清空，不能对库存添加操作，否则可能导致库存超过理论值
			if(num <= 0) {
				itemInventory.setUpdateTime(new Date());
				itemInventoryMapper.update(itemInventory);
				stringRedisTemplate.boundValueOps("itemNum_" + itemInventory.getItemId()).set(itemInventory.getNum() + "");
			} else {
				return new BaseResponse(InventoryCode.ITEM_INVENTORY_NOT_ALLOWED_ADD,InventoryMsg.ITEM_INVENTORY_NOT_ALLOWED_ADD);
			}
			return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
		} else {
			itemInventoryMapper.add(itemInventory);
			stringRedisTemplate.boundValueOps("itemNum_" + itemInventory.getItemId()).set(itemInventory.getNum() + "");
		}
		
		return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
	}

	// 批量更新库存
	@Transactional
	public BaseResponse updateBatch(List<UpdateItemNumRequest> requestList) {
		if (requestList == null) {
			return new BaseResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		}
		for (UpdateItemNumRequest updateItemNumRequest : requestList) {
			int buyItemNum = updateItemNumRequest.getBuyItemNum();
			String itemId = updateItemNumRequest.getItemId();
			String orderNumber = updateItemNumRequest.getOrderNumber();
			String userId = updateItemNumRequest.getUserId();
			if (buyItemNum <= 0) {
				throw new ItemInventoryException(InventoryMsg.ITEM_NUM_NULL);
			}
			BaseResponse update = this.update(itemId, buyItemNum, orderNumber, userId);
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	public List<ItemInventoryRecord> findItemInventoryRecordByOrderNum(String orderNumber) {
		if(StringUtils.isEmpty(orderNumber)) {
			return null;
		}
		return itemInventoryRecordMapper.findByOrderNumber(orderNumber);
	}
}
