package com.orangelala.service.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IEmailController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.auth.EmailCodeInfo;
/**
 * 邮箱服务
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/email")
public class EmailController implements IEmailController {
	
	//发送邮件
	@Override
	@GetMapping("/sendEmail")
	public BaseResponse sendEmail(String email) {
		
		return null;
	}
	
	//获取邮件验证码
	@Override
	@GetMapping("/getEmailCode")
	public EmailCodeInfo getEmailCode(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
