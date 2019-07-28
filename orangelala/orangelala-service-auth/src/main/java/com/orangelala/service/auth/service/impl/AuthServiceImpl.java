package com.orangelala.service.auth.service.impl;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.auth.request.LoginRequest;
import com.orangelala.framework.common.auth.response.LoginResponse;
import com.orangelala.framework.common.auth.response.code.AuthCode;
import com.orangelala.framework.common.auth.response.msg.AuthMsg;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.auth.AuthToken;
import com.orangelala.service.auth.service.AuthService;
/**
 * 用户密码登录方式
 * @author chrilwe
 *
 */
@Service("authService")
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@Value("${auth.applayTokenUrl}")
	private String applayTokenUrl;
	@Value("${auth.clientId}")
	private String clientId;
	@Value("${auth.clientSecret}")
	private String clientSecret;
	@Value("${auth.tokenValiditySeconds}")
	private long tokenValiditySeconds;
	
	//登录认证
	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		if(loginRequest == null) {
			return new LoginResponse(Code.PARAM_NULL,Msg.PARAM_NULL,null);
		}
		String key = loginRequest.getKey();
		String password = loginRequest.getPassword();
		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(password)) {
			return new LoginResponse(AuthCode.USERNAME_OR_PASSWORD_NULL,AuthMsg.USERNAME_OR_PASSWORD_NULL,null);
		}
		LoginResponse response = authToken(key,password);
		if(response.getCode() == AuthCode.SUCCESS) {
			AuthToken authToken = response.getAuthToken();
			boolean saveAuthToken = saveAuthToken(authToken);
			if(!saveAuthToken) {
				return new LoginResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR,null);
			} 
		}
		return response;
	}
	
	//申请令牌
	private LoginResponse authToken(String username, String password) {
		//拼接申请令牌地址
		ServiceInstance choose = loadBalancerClient.choose(ServiceList.orangelala_service_auth);
		URI uri = choose.getUri();
		String applayUrl = uri + applayTokenUrl;
		
		//发送申请令牌请求
		HttpEntity<MultiValueMap<String, String>> httpEntity = httpEntity(clientId,clientSecret,username,password);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
			public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
		});
		ResponseEntity<Map> exchange = restTemplate.exchange(applayUrl, HttpMethod.POST, httpEntity, Map.class);
		int statusCodeValue = exchange.getStatusCodeValue();
		if(statusCodeValue == AuthCode.UNAUTHORIZED) {
			return new LoginResponse(AuthCode.UNAUTHORIZED,AuthMsg.UNAUTHORIZED,null);
		}
		if(statusCodeValue == Code.SUCCESS) {
			Map body = exchange.getBody();
			String jwtToken = (String) body.get("access_token");
			String accessToken = (String) body.get("jti");
			String refreshToken = (String) body.get("refresh_token");
			if(StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(accessToken)
					|| StringUtils.isEmpty(jwtToken)) {
				return new LoginResponse(AuthCode.ERROR_TOKEN,AuthMsg.ERROR_TOKEN,null);
			}
			AuthToken authToken = new AuthToken();
			authToken.setAccessToken(accessToken);
			authToken.setJwtToken(jwtToken);
			authToken.setRefreshToken(refreshToken);
			return new LoginResponse(AuthCode.SUCCESS,AuthMsg.SUCCESS,authToken);
		}
		return null;
	}
	
	//将令牌存入redis
	private boolean saveAuthToken(AuthToken authToken) {
		try {
			stringRedisTemplate.boundValueOps(authToken.getAccessToken())
							   .set(JSON.toJSONString(authToken), 
									   tokenValiditySeconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//设置请求体和请求头
	private HttpEntity<MultiValueMap<String, String>> httpEntity(String clientId, String clientSecret, String username, String password) {
		//对clientId:clientSecrect进行base64编码
		String string = clientId + ":" + clientSecret;
		byte[] encode = Base64Utils.encode(string.getBytes());
		String httpBasic = "Basic " + new String(encode);
		
		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", httpBasic);

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("username", username);
		body.add("password", password);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);
		return httpEntity;
	}
	
}
