package com.orangelala.framework.common.ucenter.request;

import lombok.Data;

@Data
public class PhoneRegisterRequest {
	private String phoneNumber;
	private String verificationCode;
	private String password1;
	private String password2;
}
