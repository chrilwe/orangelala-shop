package com.orangelala.framework.model.order;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderCoupon {
	private int id;
	private String orderNumber;
	private int couponId;
}
