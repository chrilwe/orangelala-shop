package com.orangelala.service.im.server.auth.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.auth.response.code.AuthCode;
import com.orangelala.framework.common.auth.response.msg.AuthMsg;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.im.response.ImAuthResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.im.type.UserType;
import com.orangelala.framework.model.ucenter.User;
import com.orangelala.service.im.server.auth.ImAuthDetail;

/**
 * 验证方式的具体实现
 * 
 * @author chrilwe
 *
 */
@Component
public class ImAuthDetailImpl implements ImAuthDetail {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Value("${im.getUserUrl}")
	private String getUserUrl;

	@Override
	public ImAuthResponse accessValidate(String accessToken, String jwtToken) {
		if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(jwtToken)) {
			return new ImAuthResponse(Code.PARAM_NULL, Msg.PARAM_NULL, "", UserType.vip);
		}
		//校验并且解析令牌
		SignatureVerifier verifier = new RsaVerifier(getPublicKey());
		Jwt decodeAndVerify = null;
		try {
			decodeAndVerify = JwtHelper.decodeAndVerify(jwtToken, verifier);
		} catch (Exception e) {
			e.printStackTrace();
			//非法的令牌
			return new ImAuthResponse(AuthCode.UNAUTHORIZED,AuthMsg.UNAUTHORIZED,"",UserType.vip);
		}
		String claims = decodeAndVerify.getClaims();
		if(StringUtils.isEmpty(claims)) {
			return new ImAuthResponse(AuthCode.ERROR_TOKEN,AuthMsg.ERROR_TOKEN,"",UserType.vip);
		}
		Map map = JSON.parseObject(claims, Map.class);
		String username = (String) map.get("username");
		return new ImAuthResponse(Code.SUCCESS,Msg.SUCCESS,username,UserType.vip);
	}

	@Override
	public BaseResponse usernameValidate(String username, String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new BaseResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		}
		ServiceInstance choose = loadBalancerClient.choose(ServiceList.orangelala_service_ucenter);
		URI uri = choose.getUri();
		String ucenterUrl = uri + getUserUrl;
		ResponseEntity<User> response = restTemplate.getForEntity(ucenterUrl, User.class);
		if (response.getStatusCodeValue() == Code.SUCCESS) {
			User user = response.getBody();
			if (user == null) {
				return new BaseResponse(AuthCode.UNAUTHORIZED, AuthMsg.UNAUTHORIZED);
			}
			return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
		}
		return new BaseResponse(AuthCode.UNAUTHORIZED, AuthMsg.UNAUTHORIZED);
	}

	// 获取公钥
	private static String getPublicKey() {
		Resource resource = new ClassPathResource("publickey.txt");
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
			BufferedReader br = new BufferedReader(inputStreamReader);
			return br.lines().collect(Collectors.joining("\n"));
		} catch (Exception e) {
			return null;
		}
	}
	
	//更新登录状态
	@Override
	public BaseResponse updateLoginStatus(String status, String username) {
		if(StringUtils.isEmpty(status) || StringUtils.isEmpty(username)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		
		return null;
	}
}
