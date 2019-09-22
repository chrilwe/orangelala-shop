package com.orangelala.service.ucenter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.orangelala.framework.model.ucenter.User;

public interface UserMapper {
	
	//根据用户账号查询
	public User findByUname(String username);
	
	//根据邮箱号查询
	public User findByEmail(String email);
	
	//根据手机号查询
	public User findByPhone(String phone);
	
	//添加用户
	public int add(User user);
}
