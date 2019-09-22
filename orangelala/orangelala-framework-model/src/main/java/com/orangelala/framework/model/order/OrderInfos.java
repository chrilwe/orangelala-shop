package com.orangelala.framework.model.order;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderInfos {
	private Order order;
	private OrderAddress orderAddress;
	private List<OrderDetails> orderDetails;
}
