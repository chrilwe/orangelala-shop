package com.orangelala.service.ucenter.mapper.sql;

public interface UserSqlString {
	public static final String findByUname = "select id,username,password,salt,name,utype,birthday,userpic,sex,email,phone,status,"
			+ "create_time as createTime,update_time as updateTime from tb_user where username=#{username}";
	public static final String findByEmail = "select id,username,password,salt,name,utype,birthday,userpic,sex,email,phone,status,"
			+ "create_time as createTime,update_time as updateTime from tb_user where email=#{email}";
	public static final String findByPhone = "select id,username,password,salt,name,utype,birthday,userpic,sex,email,phone,status,"
			+ "create_time as createTime,update_time as updateTime from tb_user where phone=#{phone}";
	public static final String add = "insert into tb_user(id,username,password,salt,name,utype,birthday,userpic,sex,email,"
			+ "phone,status,create_time,update_time) values(#{id},#{username},#{password},#{salt},"
			+ "#{name},#{utype},#{birthday},#{userpic},#{sex},#{email},"
			+ "#{phone},#{status},#{createTime},#{updateTime})";
}
