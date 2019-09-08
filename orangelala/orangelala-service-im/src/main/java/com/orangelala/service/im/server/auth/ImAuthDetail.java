package com.orangelala.service.im.server.auth;
/**
 * 验证方式实现细节
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.im.response.ImAuthResponse;

public interface ImAuthDetail {
	//令牌验证方式
	public ImAuthResponse accessValidate(String accessToken, String jwtToken);
	//账号密码验证方式
	public BaseResponse usernameValidate(String username, String password);
	//更新登录状态
	public BaseResponse updateLoginStatus(String status, String username);
}
