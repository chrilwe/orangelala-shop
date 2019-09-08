package com.orangelala.framework.api;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.ucenter.request.EmailRegisterRequest;
import com.orangelala.framework.common.ucenter.request.PhoneRegisterRequest;
import com.orangelala.framework.common.ucenter.response.RegisterResponse;
import com.orangelala.framework.model.auth.SmsCodeInfo;
import com.orangelala.framework.model.ucenter.User;
/**
 * 用户中心
 * @author chrilwe
 *
 */
public interface IUcenterController {
	//根据用户账号或者手机号或者邮箱号查询用户信息
	public User findByUnamePhoneEmail(String key);
	//邮箱注册
	public RegisterResponse emailRegister(EmailRegisterRequest registerRequest);
	//手机注册
	public RegisterResponse phoneRegister(PhoneRegisterRequest registerRequest);
	//发送手机验证码
	public BaseResponse sendSmsCode(String phoneNumber);
	//发送邮箱验证码
	public BaseResponse sendEmailCode(String email);
}
