package com.orangelala.service.ucenter.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IUcenterController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.ucenter.request.EmailRegisterRequest;
import com.orangelala.framework.common.ucenter.request.PhoneRegisterRequest;
import com.orangelala.framework.common.ucenter.response.RegisterResponse;
import com.orangelala.framework.common.ucenter.response.code.UcenterCode;
import com.orangelala.framework.common.ucenter.response.msg.UcenterMsg;
import com.orangelala.framework.model.auth.SmsCodeInfo;
import com.orangelala.framework.model.ucenter.User;
import com.orangelala.service.ucenter.annotation.TestAnnotation;
import com.orangelala.service.ucenter.service.SmsEmailService;
import com.orangelala.service.ucenter.service.UserService;

@RestController
@RequestMapping("/ucenter")
public class UcenterController implements IUcenterController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private SmsEmailService smsEmailService;
	
	//根据账号，手机号，邮箱查询
	@Override
	@GetMapping("/findByUnamePhoneEmail")
	public User findByUnamePhoneEmail(String key) {
		User user = userService.findByUnamePhoneEmail(key);
		return user;
	}

	//邮箱注册
	@Override
	@PostMapping("/emailRegister")
	public RegisterResponse emailRegister(EmailRegisterRequest registerRequest) {
		
		return userService.emailRegister(registerRequest);
	}
	
	//手机号注册
	@Override
	@PostMapping("/phoneRegister")
	public RegisterResponse phoneRegister(PhoneRegisterRequest registerRequest) {
		
		return userService.phoneRegister(registerRequest);
	}
	
	//发送手机验证码
	@Override
	@GetMapping("/sendSmsCode")
	public BaseResponse sendSmsCode(String phoneNumber) {
		
		return smsEmailService.sendSmsCode(phoneNumber);
	}
	
	//发送邮箱验证码
	@Override
	@GetMapping("/sendEmailCode")
	public BaseResponse sendEmailCode(String email) {
		
		return smsEmailService.sendEmailCode(email);
	}
	
	@TestAnnotation
	@GetMapping("/test")
	public void test() {
		System.out.println("hello");
	}
}
