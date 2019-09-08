package com.orangelala.service.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IInventoryController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.inventory.exception.ItemInventoryException;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.item.inventory.response.code.InventoryCode;
import com.orangelala.framework.common.item.inventory.response.msg.InventoryMsg;
import com.orangelala.framework.model.item.inventory.ItemInventory;
import com.orangelala.framework.model.item.inventory.ItemInventoryRecord;
import com.orangelala.service.inventory.service.ItemInventoryService;
/**
 * 商品库存服务
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/inventory")
public class ItemInventoryController implements IInventoryController {
	
	@Autowired
	private ItemInventoryService itemInventoryService;
	
	//查询商品库存
	@Override
	@GetMapping("/query")
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request) {
		
		return itemInventoryService.queryItemNum(request);
	}
	
	//更新商品库存
	@Override
	@PostMapping("/update")
	public BaseResponse updateItemNumResponse(UpdateItemNumRequest request) {
		
		return itemInventoryService.updateItemNumResponse(request);
	}
	
	//添加商品库存
	@Override
	@PostMapping("/add")
	public BaseResponse addItemInventory(ItemInventory itemInventory) {
		
		return itemInventoryService.addItemInventory(itemInventory);
	}
	
	//批量更新
	@Override
	@PostMapping("/updateBatch")
	public BaseResponse updateBatch(List<UpdateItemNumRequest> requestList) {
		BaseResponse response = null;
		try {
			response = itemInventoryService.updateBatch(requestList);
		} catch (ItemInventoryException e) {
			String message = e.getMessage();
			if(message.equals(InventoryMsg.ITEM_NUM_NULL)) {
				return new BaseResponse(InventoryCode.ITEM_NUM_NULL,InventoryMsg.ITEM_NUM_NULL);
			} else if(message.equals(InventoryMsg.ITEM_INVENTORY_NO_ENOUGH)) {
				return new BaseResponse(InventoryCode.ITEM_INVENTORY_NO_ENOUGH,InventoryMsg.ITEM_INVENTORY_NO_ENOUGH);
			}
		}
		return response;
	}

	@Override
	@GetMapping("/findItemInventoryRecordByOrderNum")
	public List<ItemInventoryRecord> findItemInventoryRecordByOrderNum(String orderNumber) {
		
		return itemInventoryService.findItemInventoryRecordByOrderNum(orderNumber);
	}

}
