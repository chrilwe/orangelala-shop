package com.orangelala.framework.common.message.response.code;

import com.orangelala.framework.common.base.Code;

public class MessageCode extends Code {
	public static final int MESSAGE_NOT_EXISTS = 404;
	public static final int MESSAGE_STATUS_ERROR = 405;
	public static final int ANY_PARAM_IS_NULL = 406;
	public static final int RESENDTIMES_BIGGER_THAN_MAX = 500;
}
