package com.orangelala.framework.common.auth.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class LoginRequest extends BaseRequest {
	
	//邮箱号或者手机号或者账号
	private String key;
	//密码
	private String password;
}
