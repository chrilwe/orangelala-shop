package com.orangelala.framework.common.im.response;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.ucenter.User;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ImAuthResponse extends BaseResponse {
	private String username;
	private String userType;
	public ImAuthResponse(int code, String msg, String username, String userType) {
		super(code, msg);
		this.username = username;
		this.userType = userType;
	}

}
