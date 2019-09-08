package com.orangelala.service.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.services.ServiceList;

@FeignClient(ServiceList.orangelala_service_item)
@RequestMapping("/item")
public interface ItemClient {
	
}
