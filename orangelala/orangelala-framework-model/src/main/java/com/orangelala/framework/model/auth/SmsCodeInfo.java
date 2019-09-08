package com.orangelala.framework.model.auth;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SmsCodeInfo {
	private String phoneNumber;//发送到手机
	private String code;//验证码
	private Date sendTime;//上次发送时间
}
