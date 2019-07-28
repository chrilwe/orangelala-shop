package com.orangelala.framework.common.shop.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AggreeOpenShopResponse extends BaseResponse {

	public AggreeOpenShopResponse(int code, String msg, String username, String shopId) {
		super(code, msg);
		this.username = username;
		this.shopId = shopId;
	}
	private String username;
	private String shopId;
}
