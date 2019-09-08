package com.orangelala.framework.model.im;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 连接数据模型
 * @author chrilwe
 *
 */
@Data
@ToString
public class ConnectionModel implements Serializable {
	//身份令牌
	private String accessToken;
	//jwt令牌
	private String jwtToken;
	//用户账号
	private String username;
	//用户密码
	private String password;
	//用户身份类型：游客，登录用户   游客不需要登录也能发送消息
	private String userType;
	//认证方式
	private String authType;
}
