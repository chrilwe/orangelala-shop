package com.orangelala.framework.common.order.response.msg;

import com.orangelala.framework.common.base.Msg;

public class OrderMsg extends Msg {
	public static final String ORDER_NOT_EXISTS = "this order is not exists!";
	public static final String STATUS_ERROR = "this order status is error!";
	public static final String ORDER_PRICE_ERROR = "this order price is a error number!";
	public static final String SHOPCART_NOT_INCLUDE_ITEM = "there is a item not exists in cart!";
	public static final String PRICE_CHANGED = "there is some item price has changed!";
}
