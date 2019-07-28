package com.orangelala.framework.common.item.price.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class QueryItemPriceRequest extends BaseRequest {
	private String itemId;
}
