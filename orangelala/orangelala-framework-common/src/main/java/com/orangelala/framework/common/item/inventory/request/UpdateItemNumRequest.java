package com.orangelala.framework.common.item.inventory.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class UpdateItemNumRequest extends BaseRequest {
	private int buyItemNum;//购买数量
	private String itemId;//商品id
	private String orderNumber;//订单号
	private String userId;//用户id
}
