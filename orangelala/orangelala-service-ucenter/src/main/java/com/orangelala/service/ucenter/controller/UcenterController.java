package com.orangelala.service.ucenter.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IUcenterController;
import com.orangelala.framework.common.ucenter.request.RegisterRequest;
import com.orangelala.framework.common.ucenter.response.RegisterResponse;
import com.orangelala.framework.common.ucenter.response.code.UcenterCode;
import com.orangelala.framework.common.ucenter.response.msg.UcenterMsg;
import com.orangelala.framework.model.ucenter.User;
import com.orangelala.service.ucenter.service.UserService;

@RestController
@RequestMapping("/ucenter")
public class UcenterController implements IUcenterController {
	
	@Autowired
	private UserService userService;
	
	@Override
	@GetMapping("/findByUnamePhoneEmail")
	public User findByUnamePhoneEmail(String key) {
		User user = userService.findByUnamePhoneEmail(key);
		return user;
	}

	@Override
	@PostMapping("/register")
	public RegisterResponse register(RegisterRequest registerRequest) {
		RegisterResponse register = null;
		try {
			register = userService.register(registerRequest);
		} catch (Exception e) {
			e.printStackTrace();
			return new RegisterResponse(UcenterCode.SYSTEM_ERROR,UcenterMsg.SYSTEM_ERROR);
		}
		return register;
	}

}
