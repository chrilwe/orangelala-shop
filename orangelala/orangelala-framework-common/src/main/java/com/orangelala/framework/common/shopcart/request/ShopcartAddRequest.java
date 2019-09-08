package com.orangelala.framework.common.shopcart.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class ShopcartAddRequest extends BaseRequest {
	private String itemId;
	private int num;
}
