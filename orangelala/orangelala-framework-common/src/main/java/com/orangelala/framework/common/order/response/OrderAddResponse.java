package com.orangelala.framework.common.order.response;

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderAddResponse extends BaseResponse {

	public OrderAddResponse(int code, String msg, String orderNumber, int price) {
		super(code, msg);
		this.orderNumber = orderNumber;
		this.price = price;
	}
	
	private String orderNumber;//订单号
	private int price;//总金额
	private List<String> itemIdList;//商品库存不足的id
}
