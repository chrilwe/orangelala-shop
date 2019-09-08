package com.orangelala.service.im.server.auth.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.auth.response.code.AuthCode;
import com.orangelala.framework.common.auth.response.msg.AuthMsg;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.im.response.ImAuthResponse;
import com.orangelala.framework.common.im.response.code.ImCode;
import com.orangelala.framework.common.im.response.msg.ImMsg;
import com.orangelala.framework.model.im.ConnectionModel;
import com.orangelala.framework.model.im.MessageModel;
import com.orangelala.framework.model.im.status.ImStatus;
import com.orangelala.framework.model.im.type.UserType;
import com.orangelala.service.im.server.auth.ImAuth;
import com.orangelala.service.im.server.auth.ImAuthDetail;
/**
 * IM通讯认证
 * @author chrilwe
 *
 */
@Component
public class ImAuthImpl implements ImAuth {
	
	private static final String IM_LOGIN_KEY = "im_login_";
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ImAuthDetail imAuthDetail;

	@Override
	public ImAuthResponse login(String message) {
		MessageModel messageModel = JSON.parseObject(message, MessageModel.class);
		if(messageModel != null) {
			//判断登录客户端身份
			String userType = messageModel.getUserType();
			if(userType.equals(UserType.tourist)) {
				// 游客身份
				return new ImAuthResponse(Code.SUCCESS,Msg.SUCCESS,null,UserType.tourist);
			} else if(userType.equals(UserType.vip)) {
				// vip用户
				String accessToken = messageModel.getAccessToken();
				String username = messageModel.getUsername();
				String jwt = messageModel.getJwtToken();
				if(StringUtils.isEmpty(accessToken) && StringUtils.isEmpty(username)) {
					return new ImAuthResponse(AuthCode.UNAUTHORIZED,AuthMsg.UNAUTHORIZED,null,UserType.vip);
				}
				//优先令牌校验方式,解析令牌获取username
				if(!StringUtils.isEmpty(accessToken) && !StringUtils.isEmpty(jwt)) {
					// 从Bearer 后边开始取出token
					String token = jwt.substring(7);
					ImAuthResponse response = imAuthDetail.accessValidate(accessToken, token);
					if(response.getCode() == Code.SUCCESS) {
						//从Redis中获取用户登录信息
						String un = response.getUsername();
						String loginValue = stringRedisTemplate.boundValueOps(IM_LOGIN_KEY+un).get();
						if(!StringUtils.isEmpty(loginValue)) {
							return new ImAuthResponse(Code.SUCCESS,Msg.SUCCESS,un,UserType.vip);
						} else {
							// 修改用户登录状态
							BaseResponse re= imAuthDetail.updateLoginStatus(ImStatus.ONLINE, un);
							if(re.getCode() == Code.SUCCESS) {
								stringRedisTemplate.boundValueOps(IM_LOGIN_KEY+un).set("online");
							}
							return new ImAuthResponse(Code.SUCCESS,Msg.SUCCESS,un,UserType.vip);
						}
					}
				}
				if(!StringUtils.isEmpty(username)) {
					//从redis查询当前用户是否已经登录
					String loginValue = stringRedisTemplate.boundValueOps(IM_LOGIN_KEY+username).get();
					if(!StringUtils.isEmpty(loginValue)) {
						return new ImAuthResponse(Code.SUCCESS,Msg.SUCCESS,username,UserType.vip);
					}
					//没有登录，执行登录业务逻辑
					String password = messageModel.getPassword();
					if(!StringUtils.isEmpty(password)) {
						BaseResponse response = imAuthDetail.usernameValidate(username, password);
						if(response.getCode() == Code.SUCCESS) {
							return new ImAuthResponse(Code.SUCCESS,Msg.SUCCESS,username,UserType.vip);
						}
					}
				}
			} else {
				//非法身份
				return new ImAuthResponse(ImCode.ERROR_USER_TYPE,ImMsg.ERROR_USER_TYPE,null,UserType.errorType);
			}
		} else {
			return new ImAuthResponse(Code.PARAM_NULL,Msg.PARAM_NULL,null,"");
		}
		return new ImAuthResponse(AuthCode.UNAUTHORIZED,AuthMsg.UNAUTHORIZED,null,null);
	}
	
}
