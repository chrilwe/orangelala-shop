package com.orangelala.framework.api;
/**
 * 身份认证
 * @author chrilwe
 *
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orangelala.framework.common.auth.request.LoginRequest;
import com.orangelala.framework.common.auth.response.LoginResponse;
import com.orangelala.framework.model.auth.UserInfos;

public interface IAuthController {
	//用户登录
	public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response);
	//支付宝登录
	public LoginResponse aliLogin();
	//获取用户信息
	public UserInfos userInfos(HttpServletRequest request);
	
}
