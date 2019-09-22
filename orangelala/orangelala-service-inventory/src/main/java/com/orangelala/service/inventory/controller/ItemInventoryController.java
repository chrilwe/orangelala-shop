package com.orangelala.service.inventory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IInventoryController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.inventory.exception.ItemInventoryException;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.item.inventory.response.ReduceInventoryResponse;
import com.orangelala.framework.common.item.inventory.response.code.InventoryCode;
import com.orangelala.framework.common.item.inventory.response.msg.InventoryMsg;
import com.orangelala.framework.model.item.inventory.ItemInventory;
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
	public QueryItemNumResponse queryItemNum(@RequestParam("request")QueryItemNumRequest request) {
		
		return itemInventoryService.queryItemNum(request);
	}
	
	//减少库存
	@Override
	@GetMapping("/reduce")
	public ReduceInventoryResponse reduce(@RequestParam("map")Map<String, Integer> map) {
		
		return itemInventoryService.reduce(map);
	}
	
	//添加库存
	@Override
	@PostMapping("/increase")
	public BaseResponse increase(@RequestBody String itemId, @RequestBody int num) {
		
		return itemInventoryService.increase(itemId, num);
	}

}
