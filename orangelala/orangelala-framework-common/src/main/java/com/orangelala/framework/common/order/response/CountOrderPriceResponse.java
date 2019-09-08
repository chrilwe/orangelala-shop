package com.orangelala.framework.common.order.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CountOrderPriceResponse extends BaseResponse {

	public CountOrderPriceResponse(int code, String msg, int initPrice, int price) {
		super(code, msg);
		this.initPrice = initPrice;
		this.price = price;
		this.activityCount = initPrice - price;
	}
	
	private int initPrice;//支付总额
	private int price;//实际支付
	private int activityCount;//优惠额度
}
