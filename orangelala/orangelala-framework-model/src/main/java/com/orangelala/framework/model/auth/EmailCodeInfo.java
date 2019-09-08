package com.orangelala.framework.model.auth;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EmailCodeInfo {
	private String email;
	private String verificationCode;
	private Date sendTime;
}
