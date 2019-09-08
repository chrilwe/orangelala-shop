package com.orangelala.framework.common.order.request;

import com.orangelala.framework.common.base.BaseRequest;
import com.orangelala.framework.model.order.Order;

import lombok.Data;

@Data
public class CountOrderPriceRequest extends BaseRequest {
	private Order order;
	private String itemIds;
	private String couponIds;
}
