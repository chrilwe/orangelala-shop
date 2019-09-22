package com.orangelala.framework.model.order;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 订单基本信息
 * 
 * @author chrilwe
 *
 */
@Data
@ToString
public class Order {

	private String orderNumber;
	private String addressId;
	private int initPrice;
	private int price;
	private Date createTime;
	private int expireSec;
	private String status;
	private String userId;
	private String details;//买家留言
	private String payDetailId;//支付类型:银联支付，支付宝支付，微信支付
	private String shippingId;//物流类型：圆通快递，申通快递，韵达快递，顺丰快递，中通快递 
	private String shopId;
}
