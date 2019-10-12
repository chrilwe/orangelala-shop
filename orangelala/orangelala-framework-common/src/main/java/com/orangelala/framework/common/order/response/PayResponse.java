package com.orangelala.framework.common.order.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayResponse extends BaseResponse {

	public PayResponse(int code, String msg, String orderNumber, int totalPay) {
		super(code, msg);
		this.orderNumber = orderNumber;
		this.totalPay = totalPay;
	}
	
	private String orderNumber;//订单号
	private int totalPay;//总支付金额
}
