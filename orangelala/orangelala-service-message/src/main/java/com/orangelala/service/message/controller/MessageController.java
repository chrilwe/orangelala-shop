package com.orangelala.service.message.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IMessageController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.message.request.QueryByQueueAndStatusRequest;
import com.orangelala.framework.model.message.MessageModel;
import com.orangelala.service.message.service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController implements IMessageController {
	
	@Autowired
	private MessageService messageService;
	
	//消息准备发送
	@Override
	@PostMapping("/readysend")
	public BaseResponse readySendMessage(MessageModel messageModel) {
		BaseResponse response = null;
		try {
			response = messageService.readySendMessage(messageModel);
		} catch (Exception e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return response;
	}
	
	//消息发送中
	@Override
	@GetMapping("/sending")
	public BaseResponse sendingMessage(String messageId) {
		BaseResponse response = null;
		try {
			response = messageService.sendingMessage(messageId);
		} catch (Exception e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return response;
	}
	
	//消息已发送
	@Override
	@GetMapping("/sended")
	public BaseResponse sendedMessage(String messageId) {
		BaseResponse response = null;
		try {
			response = messageService.sendedMessage(messageId);
		} catch (Exception e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return response;
	}
	
	//消息重新发送
	@Override
	@GetMapping("/resend")
	public BaseResponse resendMessage(String messageId) {
		BaseResponse response = null;
		try {
			response = messageService.resendMessage(messageId);
		} catch (Exception e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return response;
	}
	
	//分页查询
	@Override
	@GetMapping("/queryByQueueAndStatus")
	public List<MessageModel> queryByQueueAndStatus(QueryByQueueAndStatusRequest request) {
		
		return messageService.queryByQueueAndStatus(request);
	}

	@Override
	@GetMapping("/deleteById")
	public BaseResponse deleteMessageById(String messageId) {
		
		return messageService.deleteMessageById(messageId);
	}

}
