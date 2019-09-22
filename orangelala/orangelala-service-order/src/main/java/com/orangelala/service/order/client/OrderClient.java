package com.orangelala.service.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.services.ServiceList;

@FeignClient(ServiceList.orangelala_service_order)
@RequestMapping("/order")
public interface OrderClient {
	
	@PostMapping("/add")
	public BaseResponse addOrder(@RequestBody OrderAddRequest orderAddRequest);
}
