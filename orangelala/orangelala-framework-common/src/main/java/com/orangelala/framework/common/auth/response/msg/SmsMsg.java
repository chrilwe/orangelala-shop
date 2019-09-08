package com.orangelala.framework.common.auth.response.msg;

import com.orangelala.framework.common.base.Msg;

public class SmsMsg extends Msg {
	public static final String PHONE_NUMBER_NULL = "The phone number is not allowed null!";
	public static final String PLEASE_SEND_AFTER_60_SEC = "Please send code after 60s!";
}
