package com.orangelala.service.auth.service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.auth.SmsCodeInfo;

public interface SmsService {
	//发送验证码
	public BaseResponse sendSmsVerificationCode(String phoneNumber);
	
	//获取验证码
	public SmsCodeInfo getSmsCodeInfo(String phoneNumber);
}
