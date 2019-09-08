package com.orangelala.service.shopcart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.item.ItemInfo;

@FeignClient(ServiceList.orangelala_service_item)
@RequestMapping("/item")
public interface ItemClient {
	
	@GetMapping("/iteminfo/find")
	public ItemInfo findItemInfoById(String itemId);
}
