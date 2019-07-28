package com.orangelala.service.item.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.item.inventory.ItemInventory;

/**
 * 库存服务客户端
 * @author chrilwe
 *
 */
@FeignClient(ServiceList.orangelala_service_inventory)
@RequestMapping("/inventory")
public interface ItemInventoryClient {
	
	@GetMapping("/query")
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request);
	
	@PostMapping("/update")
	public BaseResponse updateItemNumResponse(UpdateItemNumRequest request);
	
	@PostMapping("/add")
	public BaseResponse addItemInventory(ItemInventory itemInventory);
}
