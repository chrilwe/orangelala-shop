package com.orangelala.service.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.ISmsController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.auth.SmsCodeInfo;
import com.orangelala.service.auth.service.SmsService;
/**
 * 短信服务
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/sms")
public class SmsController implements ISmsController {
	
	@Autowired
	private SmsService smsService;
	
	//发送短信验证码
	@Override
	@PostMapping("/send/verificationcode")
	public BaseResponse sendSmsVerificationCode(String phoneNumber) {
		
		return smsService.sendSmsVerificationCode(phoneNumber);
	}
	
	//获取短信验证码
	@Override
	@GetMapping("/get/code")
	public SmsCodeInfo getSmsCodeInfo(String phoneNumber) {
		
		return smsService.getSmsCodeInfo(phoneNumber);
	}

}
