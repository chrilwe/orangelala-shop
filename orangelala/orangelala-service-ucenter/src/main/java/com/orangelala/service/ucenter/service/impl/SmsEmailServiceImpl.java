package com.orangelala.service.ucenter.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.ucenter.response.code.UcenterCode;
import com.orangelala.framework.common.ucenter.response.msg.UcenterMsg;
import com.orangelala.framework.model.ucenter.User;
import com.orangelala.service.ucenter.client.SmsClient;
import com.orangelala.service.ucenter.service.SmsEmailService;
import com.orangelala.service.ucenter.service.UserService;

@Service
public class SmsEmailServiceImpl implements SmsEmailService {
	
	@Autowired
	private SmsClient smsClient;
	@Autowired
	private UserService userService;
	
	//发送手机验证码
	@Override
	public BaseResponse sendSmsCode(String phoneNumber) {
		if(StringUtils.isEmpty(phoneNumber)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		//校验当前手机号码是否已经被注册
		User user = userService.findByPhone(phoneNumber);
		if(user != null) {
			return new BaseResponse(UcenterCode.PHONE_IS_REGISTERED,UcenterMsg.PHONE_IS_REGISTERED);
		}
		//发送验证码
		BaseResponse result = smsClient.sendSmsVerificationCode(phoneNumber);
		return result;
	}
	
	//发送邮箱验证码
	@Override
	public BaseResponse sendEmailCode(String email) {
		if(StringUtils.isEmpty(email)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		//校验邮箱是否已经被注册
		User user = userService.findByEmail(email);
		if(user != null) {
			return new BaseResponse(UcenterCode.EMAIL_IS_REGISTERED,UcenterMsg.EMAIL_IS_REGISTERED);
		}
		//发送邮件
		return null;
	}

}
