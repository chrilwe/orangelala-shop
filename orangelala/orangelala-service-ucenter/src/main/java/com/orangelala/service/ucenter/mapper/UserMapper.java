package com.orangelala.service.ucenter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.orangelala.framework.model.ucenter.User;
import com.orangelala.service.ucenter.mapper.sql.UserSqlString;

public interface UserMapper {
	
	//根据用户账号查询
	@Select(UserSqlString.findByUname)
	public User findByUname(String username);
	
	//根据邮箱号查询
	@Select(UserSqlString.findByEmail)
	public User findByEmail(String email);
	
	//根据手机号查询
	@Select(UserSqlString.findByPhone)
	public User findByPhone(String phone);
	
	//添加用户
	@Insert(UserSqlString.add)
	public int add(User user);
}
