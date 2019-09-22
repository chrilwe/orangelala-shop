package com.orangelala.service.order.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.item.inventory.response.ReduceInventoryResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.item.inventory.ItemInventoryRecord;

@FeignClient(ServiceList.orangelala_service_inventory)
@RequestMapping("/inventory")
public interface InventoryClient {
	
	@GetMapping("/query")
	public QueryItemNumResponse queryItemNum(@RequestParam("request")QueryItemNumRequest request);
	
	@GetMapping("/reduce")
	public ReduceInventoryResponse reduce(@RequestParam("map")Map<String, Integer> map);
	
	@PostMapping("/increase")
	public BaseResponse increase(@RequestBody String itemId, @RequestBody int num);
	
}
