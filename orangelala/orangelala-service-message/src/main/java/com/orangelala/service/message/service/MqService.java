package com.orangelala.service.message.service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.message.MessageModel;

public interface MqService {
	public BaseResponse sendMessage(MessageModel messageModel);
}
