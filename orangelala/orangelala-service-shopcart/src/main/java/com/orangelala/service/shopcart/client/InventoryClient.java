package com.orangelala.service.shopcart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.services.ServiceList;

@FeignClient(ServiceList.orangelala_service_inventory)
@RequestMapping("/inventory")
public interface InventoryClient {
	
	@GetMapping("/query")
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request);
}
