package com.orangelala.framework.model.auth;

import lombok.Data;
import lombok.ToString;

/**
 * 用户展示信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class UserInfos extends AuthToken {
	private String userId;
	private String username;
	private String name;
	private String userPic;
	private String utype;
}
