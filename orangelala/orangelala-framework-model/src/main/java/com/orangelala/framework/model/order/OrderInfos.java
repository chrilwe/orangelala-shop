package com.orangelala.framework.model.order;

import java.util.List;

import lombok.Data;
import lombok.ToString;
/**
 * 订单所有信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class OrderInfos {
	private Order order;
	private OrderAddress orderAddress;
	private List<OrderDetails> orderDetails;
}
