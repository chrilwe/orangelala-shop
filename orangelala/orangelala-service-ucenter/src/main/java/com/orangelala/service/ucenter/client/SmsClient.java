package com.orangelala.service.ucenter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.auth.SmsCodeInfo;

@FeignClient(ServiceList.orangelala_service_auth)
@RequestMapping("/sms")
public interface SmsClient {
	
	@PostMapping("/send/verificationcode")
	public BaseResponse sendSmsVerificationCode(String phoneNumber);
	
	@GetMapping("/get/code")
	public SmsCodeInfo getSmsCodeInfo(String phoneNumber);
}
