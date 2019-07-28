package com.orangelala.framework.api;
/**
 * 用户中心
 * @author chrilwe
 *
 */
import com.orangelala.framework.common.ucenter.request.RegisterRequest;
import com.orangelala.framework.common.ucenter.response.RegisterResponse;
import com.orangelala.framework.model.ucenter.User;

public interface IUcenterController {
	//根据用户账号或者手机号或者邮箱号查询用户信息
	public User findByUnamePhoneEmail(String key);
	//注册用户
	public RegisterResponse register(RegisterRequest registerRequest);
	
}
