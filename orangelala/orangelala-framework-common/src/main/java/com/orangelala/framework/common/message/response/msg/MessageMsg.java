package com.orangelala.framework.common.message.response.msg;

import com.orangelala.framework.common.base.Msg;

public class MessageMsg extends Msg {
	public static final String MESSAGE_NOT_EXISTS = "this message is not exists!";
	public static final String MESSAGE_STATUS_ERROR = "this message status is error!";
	public static final String ANY_PARAM_IS_NULL = "it is not allow any params null!";
	public static final String ROUTINGKEY_OR_QUEUENAME_ERROR = "rabbitmq routingkey or queueName error!";
	public static final String ROUTINGKEY_ERROR = "this routingKey is not right!";
	public static final String RESENDTIMES_BIGGER_THAN_MAX = "message resend times is bigger than max resend times!";
}
