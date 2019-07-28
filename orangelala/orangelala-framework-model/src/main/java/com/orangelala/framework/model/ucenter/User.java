package com.orangelala.framework.model.ucenter;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 用户
 * 
 * @author chrilwe
 *
 */
@Data
@ToString
public class User {

	private String id;
	private String username;
	private String password;
	private String salt;
	private String name;
	private String utype;
	private String birthday;
	private String userpic;
	private String sex;
	private String email;
	private String phone;
	private String status;
	private Date createTime;
	private Date updateTime;
}
