package com.orangelala.service.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.ucenter.User;

/**
 * 用户信息
 * @author chrilwe
 *
 */
@FeignClient(ServiceList.orangelala_service_ucenter)
@RequestMapping("/ucenter")
public interface UcenterClient {
	
	//根据账号邮箱号或者手机号查询用户
	@PostMapping("/findByUnamePhoneEmail")
	public User findByUnamePhoneEmail(String key);
}
