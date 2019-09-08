package com.orangelala.service.item.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.price.request.QueryItemPriceRequest;
import com.orangelala.framework.common.item.price.request.UpdateItemPriceRequest;
import com.orangelala.framework.common.item.price.response.QueryItemPriceResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.item.price.ItemPrice;

/**
 * 库存服务客户端
 * @author chrilwe
 *
 */
@FeignClient(ServiceList.orangelala_service_price)
@RequestMapping("/price")
public interface ItemPriceClient {
	
	@GetMapping("/query")
	public QueryItemPriceResponse queryItemPrice(QueryItemPriceRequest request);
	
	@PostMapping("/update")
	public BaseResponse updateItemPrice(UpdateItemPriceRequest request);
	
}
