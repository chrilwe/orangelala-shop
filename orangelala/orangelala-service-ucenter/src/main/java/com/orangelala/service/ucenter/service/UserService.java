package com.orangelala.service.ucenter.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.ucenter.request.RegisterRequest;
import com.orangelala.framework.common.ucenter.response.RegisterResponse;
import com.orangelala.framework.common.ucenter.response.code.UcenterCode;
import com.orangelala.framework.common.ucenter.response.msg.UcenterMsg;
import com.orangelala.framework.model.ucenter.User;
import com.orangelala.framework.model.ucenter.status.UserStatus;
import com.orangelala.framework.utils.BCryptUtil;
import com.orangelala.service.ucenter.mapper.UserMapper;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 *  根据用户账号或者手机号或者邮箱号查询用户信息
	 * @param key
	 * @return
	 */
	public User findByUnamePhoneEmail(String key) {
		if(StringUtils.isEmpty(key)) {
			
		}
		//根据用户账号查询
		User findByUsername = this.findByUsername(key);
		if(findByUsername != null) {
			return findByUsername;
		}
		
		//根据用户手机号查询
		User findByPhone = this.findByPhone(key);
		if(findByPhone != null) {
			return findByPhone;
		}
		
		//根据用户邮箱查询
		User findByEmail = this.findByEmail(key);
		if(findByEmail != null) {
			return findByEmail;
		}
		
		return null;
	}

	/**
	 *  注册用户
	 * @param registerRequest
	 * @return
	 */
	@Transactional
	public RegisterResponse register(RegisterRequest registerRequest) {
		if(registerRequest == null) {
			
		}
		User user = registerRequest.getUser();
		String password = user.getPassword();
		String phone = user.getPhone();
		String email = user.getEmail();
		String username = user.getUsername();
		//密码不能为空
		if(StringUtils.isEmpty(password)) {
			return new RegisterResponse(UcenterCode.PASSWORD_NULL,UcenterMsg.PASSWORD_NULL);
		}
		//邮箱，账号，手机号必须至少有一个不为空
		if(StringUtils.isEmpty(username)&&StringUtils.isEmpty(phone)
				&&StringUtils.isEmpty(email)) {
			
		}
		//校验邮箱，手机号的合法性
		//插入数据
		user.setCreateTime(new Date());
		user.setStatus(UserStatus.YES);
		user.setPassword(BCryptUtil.encode(password));
		userMapper.add(user);
		return new RegisterResponse(UcenterCode.SUCCESS,UcenterMsg.SUCCESS);
	}
	
	/**
	 * 根据用户账号查询
	 * @return
	 */
	public User findByUsername(String username) {
		if(StringUtils.isEmpty(username)) {
			
		}
		return userMapper.findByUname(username);
	}
	
	/**
	 * 根据用户手机号查询
	 * @return
	 */
	public User findByPhone(String phone) {
		if(StringUtils.isEmpty(phone)) {
			return null;
		}
		return userMapper.findByPhone(phone);
	}
	
	/**
	 * 根据用户邮箱查询
	 * @return
	 */
	public User findByEmail(String email) {
		if(StringUtils.isEmpty(email)) {
			return null;
		}
		return userMapper.findByEmail(email);
	}
}
