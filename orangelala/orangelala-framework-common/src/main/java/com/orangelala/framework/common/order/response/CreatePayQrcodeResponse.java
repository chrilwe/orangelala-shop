package com.orangelala.framework.common.order.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;

/**
 * 预下单支付二维码响应
 * @author chrilwe
 *
 */
@Data
@ToString
public class CreatePayQrcodeResponse extends BaseResponse {
	
	public CreatePayQrcodeResponse(int code, String msg, String payQrcodeUrl) {
		super(code, msg);
		this.payQrcodeUrl = payQrcodeUrl;
	}
	
	private String payQrcodeUrl;
}
