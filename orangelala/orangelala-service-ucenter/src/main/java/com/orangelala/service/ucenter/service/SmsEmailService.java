package com.orangelala.service.ucenter.service;
/**
 * 短信邮箱服务
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;

public interface SmsEmailService {
	//发送手机验证码
	public BaseResponse sendSmsCode(String phoneNumber);
	
	//发送邮件验证码
	public BaseResponse sendEmailCode(String email);
}
