package com.orangelala.framework.common.shop.response.code;

import com.orangelala.framework.common.base.Code;

public class ActivityCode extends Code {
	public static final int ACTIVITY_NOT_EXISTS = 404;
	public static final int ACTIVITY_END = 405;
	public static final int ACTIVITY_ENDTIME_ERROR = 406;
	public static final int ACTIVITY_STARTTIME_AFTER_ENDTIME = 407;
}
