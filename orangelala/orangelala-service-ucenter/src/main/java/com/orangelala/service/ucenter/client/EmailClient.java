package com.orangelala.service.ucenter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.auth.EmailCodeInfo;

@FeignClient(ServiceList.orangelala_service_auth)
@RequestMapping("/email")
public interface EmailClient {
	
	@GetMapping("/sendEmail")
	public BaseResponse sendEmail(String email);
	
	@GetMapping("/getEmailCode")
	public EmailCodeInfo getEmailCode(String email);
}
