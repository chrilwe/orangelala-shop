package com.orangelala.framework.common.shop.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ApplayOpenShopResponse extends BaseResponse {

	public ApplayOpenShopResponse(int code, String msg) {
		super(code, msg);
	}
}
