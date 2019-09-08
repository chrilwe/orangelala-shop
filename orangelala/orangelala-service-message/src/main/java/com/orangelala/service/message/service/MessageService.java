package com.orangelala.service.message.service;

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.message.request.QueryByQueueAndStatusRequest;
import com.orangelala.framework.model.message.MessageModel;

public interface MessageService {
	// 准备发送消息
	public BaseResponse readySendMessage(MessageModel messageModel);

	// 消息发送中
	public BaseResponse sendingMessage(String messageId);

	// 消息已发送
	public BaseResponse sendedMessage(String messageId);
	
	//消息重新发送
	public BaseResponse resendMessage(String messageId);
	
	//根据消息queue和状态分页查询
	public List<MessageModel> queryByQueueAndStatus(QueryByQueueAndStatusRequest request);

	public BaseResponse deleteMessageById(String messageId);
}
