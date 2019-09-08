package com.orangelala.service.order.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.item.price.request.QueryItemPriceRequest;
import com.orangelala.framework.common.item.price.response.QueryItemPriceResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.item.price.ItemPrice;

@FeignClient(ServiceList.orangelala_service_price)
@RequestMapping("/price")
public interface PriceClient {
	
	@GetMapping("/query")
	public QueryItemPriceResponse queryItemPrice(QueryItemPriceRequest request);
	
	@GetMapping("/queryBatch")
	public List<ItemPrice> queryPriceBatch(String itemIds);
}
