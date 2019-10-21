package com.orangelala.framework.common.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.orangelala.framework.utils.Oauth2Util;

@Controller
public class BaseController {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public String userId;

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {

		this.request = request;

		this.response = response;
		
		this.userId = this.getUserID();

	}
	
	//获取用户ID
	public String getUserID() {
		Map<String, String> jwtClaimsFromHeader = Oauth2Util.getJwtClaimsFromHeader(request);
		return jwtClaimsFromHeader.get("userId");
	}
}
