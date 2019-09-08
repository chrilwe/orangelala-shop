package com.orangelala.framework.api;
/**
 * sms短信服务
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.auth.SmsCodeInfo;

public interface ISmsController {
	//发送短信验证码
	public BaseResponse sendSmsVerificationCode(String phoneNumber);
	
	//获取短信验证码
	public SmsCodeInfo getSmsCodeInfo(String phoneNumber);
}
