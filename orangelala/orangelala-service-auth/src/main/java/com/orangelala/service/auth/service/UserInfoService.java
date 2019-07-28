package com.orangelala.service.auth.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.orangelala.framework.model.auth.UserInfos;
import com.orangelala.framework.utils.Oauth2Util;

@Service
public class UserInfoService {
	
	//获取用户信息
	public UserInfos getUserInfo(HttpServletRequest request) {
		Map<String, String> userInfoMap = Oauth2Util.getJwtClaimsFromHeader(request);
		String name = userInfoMap.get("name");
		String userId = userInfoMap.get("userId");
		String username = userInfoMap.get("username");
		String userPic = userInfoMap.get("userPic");
		String utype = userInfoMap.get("utype");
		UserInfos userInfos = new UserInfos();
		userInfos.setName(name);
		userInfos.setUserId(userId);
		userInfos.setUsername(username);
		userInfos.setUserPic(userPic);
		userInfos.setUtype(utype);
		return userInfos;
	}
}
