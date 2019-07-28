package com.orangelala.service.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IAuthController;
import com.orangelala.framework.common.auth.request.LoginRequest;
import com.orangelala.framework.common.auth.response.LoginResponse;
import com.orangelala.framework.common.auth.response.code.AuthCode;
import com.orangelala.framework.common.auth.response.msg.AuthMsg;
import com.orangelala.framework.model.auth.AuthToken;
import com.orangelala.framework.model.auth.UserInfos;
import com.orangelala.framework.utils.CookieUtil;
import com.orangelala.service.auth.service.UserInfoService;
import com.orangelala.service.auth.service.impl.AuthServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController implements IAuthController {
	
	@Autowired
	private AuthServiceImpl authService;
	@Autowired
	private UserInfoService userInfoService;
	
	@Value("${auth.cookieDomain}")
	private String domain;
	@Value("${auth.cookiePath}")
	private String path;
	@Value("${auth.cookieName}")
	private String name;
	@Value("${auth.cookieMaxAge}")
	private int maxAge;
	
	
	//当前平台账号登录
	@Override
	@PostMapping("/login")
	public LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpResponse) {
		LoginResponse result = null;
		try {
			result = authService.login(loginRequest);
			
			//认证完成，设置cookie和请求头
			if(result.getCode() == AuthCode.SUCCESS) {
				AuthToken authToken = result.getAuthToken();
				String jwtToken = authToken.getJwtToken();
				String accessToken = authToken.getAccessToken();
				CookieUtil.addCookie(httpResponse, domain, path, name, accessToken, maxAge, true);
				httpResponse.addHeader("Authorization", "Bearer " + jwtToken);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new LoginResponse(AuthCode.SYSTEM_ERROR,AuthMsg.SYSTEM_ERROR,null);
		}
		return result;
	}
	
	//获取用户信息
	@Override
	@GetMapping("/userinfo")
	public UserInfos userInfos(HttpServletRequest request) {
		UserInfos userInfo = userInfoService.getUserInfo(request);
		return userInfo;
	}
	
	//支付宝第三方登录
	@Override
	@GetMapping("/aliLogin")
	public LoginResponse aliLogin() {
		// TODO Auto-generated method stub
		return null;
	}

}
