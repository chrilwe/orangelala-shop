package com.orangelala.framework.model.auth;

import lombok.Data;
import lombok.ToString;

/**
 * 存放认证令牌
 * @author chrilwe
 *
 */
@Data
@ToString
public class AuthToken {
	
	//身份令牌
	private String accessToken;
	//jwt令牌
	private String jwtToken;
	//刷新令牌
	private String refreshToken;
}
