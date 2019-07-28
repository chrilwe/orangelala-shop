package com.orangelala.framework.utils;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;

import com.alibaba.fastjson.JSON;

public class Oauth2Util {

	public static Map<String, String> getJwtClaimsFromHeader(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		// 取出头信息
		String authorization = request.getHeader("Authorization");
		if (StringUtils.isEmpty(authorization) || authorization.indexOf("Bearer") < 0) {
			return null;
		}
		// 从Bearer 后边开始取出token
		String token = authorization.substring(7);
		Map<String, String> map = null;
		try {
			// 解析jwt
			Jwt decode = JwtHelper.decode(token);
			// 得到 jwt中的用户信息
			String claims = decode.getClaims();
			// 将jwt转为Map
			map = JSON.parseObject(claims, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
