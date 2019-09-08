package com.orangelala.base.gateway.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.orangelala.framework.common.auth.response.code.AuthCode;
import com.orangelala.framework.common.auth.response.msg.AuthMsg;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.utils.CookieUtil;

@Component
public class LoginFilter extends ZuulFilter {
	private static final String PRE = "pre";
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Value("${ignoredAuthURI}")
	private String ignoredAuthURI;
	@Value("${cookieName}")
	private String cookieName;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public Object run() throws ZuulException {
		//获取当前请求上下文
		RequestContext currentContext = RequestContext.getCurrentContext();
		HttpServletRequest request = currentContext.getRequest();
		HttpServletResponse response = currentContext.getResponse();
		
		//微服务真实请求URI
		String requestURI = request.getRequestURI();
		int length = contextPath.length();
		String realServiceURI = requestURI.substring(length - 1);
		
		//不需要认证就可以访问的微服务请求
		String[] split = ignoredAuthURI.split(",");
		boolean flag = false;
		for (String string : split) {
			if(realServiceURI.equals(string)) {
				flag = true;
				break;
			}
		}
		
		//需要认证的请求
		if(!flag) {
			String jwt  = (String) request.getAttribute("Authorization");
			if(!jwt.startsWith("Bearer ")) {
				//拒绝访问
				cantnotRequest(currentContext);
			}
			Map<String, String> cookie = CookieUtil.readCookie(request, cookieName);
			String currentCookie = cookie.get(cookieName);
			if(StringUtils.isEmpty(currentCookie)) {
				//拒绝访问
				cantnotRequest(currentContext);
			}
			String authToken = stringRedisTemplate.boundValueOps(currentCookie).get();
			if(StringUtils.isEmpty(authToken)) {
				//拒绝访问
				cantnotRequest(currentContext);
			}
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return PRE;
	}
	
	private void cantnotRequest(RequestContext context) {
		context.setSendZuulResponse(false);
		BaseResponse responseBody = new BaseResponse(AuthCode.UNAUTHORIZED,AuthMsg.UNAUTHORIZED);
		context.setResponseBody(JSON.toJSONString(responseBody));
		context.getResponse().setContentType("application/json;charset=utf-8");
	}
}
