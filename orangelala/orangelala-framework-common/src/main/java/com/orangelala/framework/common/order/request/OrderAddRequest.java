package com.orangelala.framework.common.order.request;

import java.util.List;

import com.orangelala.framework.common.base.BaseRequest;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.order.Order;

import lombok.Data;

@Data
public class OrderAddRequest extends BaseRequest {
	private Order order;
	private String itemIds;
	private String shopId;
	private String couponIds;
}
