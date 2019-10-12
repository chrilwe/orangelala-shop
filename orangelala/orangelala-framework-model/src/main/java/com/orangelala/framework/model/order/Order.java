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
	private int initPrice;//订单未优惠之前总金额
	private int price;//订单优惠后 的金额
	private Date createTime;//订单创建时间
	private int expireSec;//订单超时支付时间(秒)
	private String status;//订单状态：未支付，已支付，已撤销
	private String userId;
	private String details;//买家留言
	private String payDetailId;//支付类型:银联支付，支付宝支付，微信支付
	private String shippingId;//物流类型：圆通快递，申通快递，韵达快递，顺丰快递，中通快递 
	private String shopId;
}
