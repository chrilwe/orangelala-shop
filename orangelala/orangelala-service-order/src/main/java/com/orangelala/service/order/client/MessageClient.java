package com.orangelala.service.order.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.message.request.QueryByQueueAndStatusRequest;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.message.MessageModel;

@FeignClient(ServiceList.orangelala_service_message)
@RequestMapping("/message")
public interface MessageClient {

	// 准备发送消息
	@PostMapping("/readysend")
	public BaseResponse readySendMessage(MessageModel messageModel);

	// 消息发送中
	@GetMapping("/sending")
	public BaseResponse sendingMessage(String messageId);

	// 消息已发送
	@GetMapping("/sended")
	public BaseResponse sendedMessage(String messageId);
	
	@GetMapping("/resend")
	public BaseResponse resendMessage(String messageId);
	
	//分页查询
	@GetMapping("/queryByQueueAndStatus")
	public List<MessageModel> queryByQueueAndStatus(QueryByQueueAndStatusRequest request);

	@GetMapping("/deleteById")
	public BaseResponse deleteMessageById(String messageId);
}
