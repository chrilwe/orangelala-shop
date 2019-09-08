package com.orangelala.framework.common.ucenter.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

/**
 * 邮箱注册
 * @author chrilwe
 *
 */
@Data
public class EmailRegisterRequest extends BaseRequest {
	private String email;
	private String verificationCode;
	private String password1;
	private String password2;
}
