package com.orangelala.service.ucenter.service;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.ucenter.request.EmailRegisterRequest;
import com.orangelala.framework.common.ucenter.request.PhoneRegisterRequest;
import com.orangelala.framework.common.ucenter.response.RegisterResponse;
import com.orangelala.framework.common.ucenter.response.code.UcenterCode;
import com.orangelala.framework.common.ucenter.response.msg.UcenterMsg;
import com.orangelala.framework.model.auth.EmailCodeInfo;
import com.orangelala.framework.model.auth.SmsCodeInfo;
import com.orangelala.framework.model.ucenter.User;
import com.orangelala.framework.model.ucenter.status.UserStatus;
import com.orangelala.framework.utils.BCryptUtil;
import com.orangelala.service.ucenter.client.EmailClient;
import com.orangelala.service.ucenter.client.SmsClient;
import com.orangelala.service.ucenter.mapper.UserMapper;

@Service
public class UserService {
	private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(10,15,60,TimeUnit.SECONDS,new ArrayBlockingQueue(10));
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private SmsClient smsClient;
	@Autowired
	private EmailClient emailClient;

	/**
	 * 根据用户账号或者手机号或者邮箱号查询用户信息
	 * 
	 * @param key
	 * @return
	 */
	public User findByUnamePhoneEmail(String key) {
		if (StringUtils.isEmpty(key)) {

		}
		// 根据用户账号查询
		User findByUsername = this.findByUsername(key);
		if (findByUsername != null) {
			return findByUsername;
		}

		// 根据用户手机号查询
		User findByPhone = this.findByPhone(key);
		if (findByPhone != null) {
			return findByPhone;
		}

		// 根据用户邮箱查询
		User findByEmail = this.findByEmail(key);
		if (findByEmail != null) {
			return findByEmail;
		}

		return null;
	}

	/**
	 * 邮箱注册
	 * 
	 * @param registerRequest
	 * @return
	 */
	@Transactional
	public RegisterResponse emailRegister(EmailRegisterRequest registerRequest) {
		if (registerRequest == null) {
			return new RegisterResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		}
		String email = registerRequest.getEmail();
		String password1 = registerRequest.getPassword1();
		String password2 = registerRequest.getPassword2();
		String verificationCode = registerRequest.getVerificationCode();

		// 校验邮箱验证码
		EmailCodeInfo emailCodeInfo = emailClient.getEmailCode(email);
		if (emailCodeInfo == null || !emailCodeInfo.getVerificationCode().equals(verificationCode)) {
			return new RegisterResponse(UcenterCode.PHONE_VERIFICATION_CODE_ERROR,
					UcenterMsg.PHONE_VERIFICATION_CODE_ERROR);
		}
		// 比对密码
		if (!password1.equals(password2)) {
			return new RegisterResponse(UcenterCode.PASSWORD_NOT_SAME, UcenterMsg.PASSWORD_NOT_SAME);
		}
		// 对密码进行base64加密
		String password = BCryptUtil.encode(password1);
		//添加用户信息
		User user = new User();
		user.setCreateTime(new Date());
		user.setName(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setStatus(UserStatus.NORMAL);
		userMapper.add(user);
		//TODO  发送注册成功邮件
		pool.submit(new Runnable(){
			@Override
			public void run() {
				
			}	
		});
		return new RegisterResponse(UcenterCode.SUCCESS, UcenterMsg.SUCCESS);
	}

	@Transactional
	public RegisterResponse phoneRegister(PhoneRegisterRequest registerRequest) {
		if (registerRequest == null) {
			return new RegisterResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		}
		String password1 = registerRequest.getPassword1();
		String password2 = registerRequest.getPassword2();
		String phoneNumber = registerRequest.getPhoneNumber();
		String verificationCode = registerRequest.getVerificationCode();
		// 校验手机验证码
		SmsCodeInfo smsCodeInfo = smsClient.getSmsCodeInfo(phoneNumber);
		if (smsCodeInfo == null || !smsCodeInfo.getCode().equals(verificationCode)) {
			return new RegisterResponse(UcenterCode.PHONE_VERIFICATION_CODE_ERROR,
					UcenterMsg.PHONE_VERIFICATION_CODE_ERROR);
		}
		// 比对密码
		if (!password1.equals(password2)) {
			return new RegisterResponse(UcenterCode.PASSWORD_NOT_SAME, UcenterMsg.PASSWORD_NOT_SAME);
		}
		// 对密码进行base64加密
		String password = BCryptUtil.encode(password1);
		// 添加用户信息
		User user = new User();
		user.setPhone(phoneNumber);
		user.setStatus(UserStatus.NORMAL);
		user.setName(phoneNumber);
		user.setPassword(password);
		user.setCreateTime(new Date());
		userMapper.add(user);
		//TODO 发送注册成功消息
		pool.submit(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
		return new RegisterResponse(Code.SUCCESS, Msg.SUCCESS);
	}

	/**
	 * 根据用户账号查询
	 * 
	 * @return
	 */
	public User findByUsername(String username) {
		if (StringUtils.isEmpty(username)) {

		}
		return userMapper.findByUname(username);
	}

	/**
	 * 根据用户手机号查询
	 * 
	 * @return
	 */
	public User findByPhone(String phone) {
		if (StringUtils.isEmpty(phone)) {
			return null;
		}
		return userMapper.findByPhone(phone);
	}

	/**
	 * 根据用户邮箱查询
	 * 
	 * @return
	 */
	public User findByEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return null;
		}
		return userMapper.findByEmail(email);
	}
}
