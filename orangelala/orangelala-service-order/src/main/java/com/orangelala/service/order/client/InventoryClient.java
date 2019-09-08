package com.orangelala.service.order.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.item.inventory.ItemInventoryRecord;

@FeignClient(ServiceList.orangelala_service_inventory)
@RequestMapping("/inventory")
public interface InventoryClient {
	
	@GetMapping("/query")
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request);
	
	@PostMapping("/update")
	public BaseResponse updateItemNumResponse(UpdateItemNumRequest request);
	
	@PostMapping("/updateBatch")
	public BaseResponse updateBatch(List<UpdateItemNumRequest> requestList);
	
	@GetMapping("/findItemInventoryRecordByOrderNum")
	public List<ItemInventoryRecord> findItemInventoryRecordByOrderNum(String orderNumber);
}
