package com.orangelala.service.auth.configs;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserJwt extends User {
	private String userId;
	private String userPic;
	private String utype;
	private String name;
	public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

}
