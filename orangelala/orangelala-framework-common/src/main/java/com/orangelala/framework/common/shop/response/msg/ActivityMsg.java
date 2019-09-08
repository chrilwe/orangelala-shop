package com.orangelala.framework.common.shop.response.msg;

import com.orangelala.framework.common.base.Msg;

public class ActivityMsg extends Msg {
	public static final String ACTIVITY_NOT_EXISTS = "this activity is not exists!";
	public static final String ACTIVITY_END = "the activity is end!";
	public static final String ACTIVITY_ENDTIME_ERROR = "the activity endtime is before currentTime!";
	public static final String ACTIVITY_STARTTIME_AFTER_ENDTIME = "the activity startTime is not allowed bigger than endTime!";
}
