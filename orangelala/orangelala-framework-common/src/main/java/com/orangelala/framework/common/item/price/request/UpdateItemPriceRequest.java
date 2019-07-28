package com.orangelala.framework.common.item.price.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class UpdateItemPriceRequest extends BaseRequest {
	private String itemId;
	private long price;//商品更新后的价格
}
