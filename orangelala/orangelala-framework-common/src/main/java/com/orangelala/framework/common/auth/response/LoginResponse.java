package com.orangelala.framework.common.auth.response;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.auth.AuthToken;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginResponse extends BaseResponse {
	private AuthToken authToken;

	public LoginResponse(int code, String msg, AuthToken authToken) {
		super(code, msg);
		this.authToken = authToken;
	}

}
