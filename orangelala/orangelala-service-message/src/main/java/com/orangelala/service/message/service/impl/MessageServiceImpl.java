package com.orangelala.service.message.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.message.exception.MessageException;
import com.orangelala.framework.common.message.request.QueryByQueueAndStatusRequest;
import com.orangelala.framework.common.message.response.code.MessageCode;
import com.orangelala.framework.common.message.response.msg.MessageMsg;
import com.orangelala.framework.model.message.MessageModel;
import com.orangelala.framework.model.message.status.MessageStatus;
import com.orangelala.framework.utils.ZkUtil;
import com.orangelala.service.message.mapper.MessageMapper;
import com.orangelala.service.message.service.MessageService;
import com.orangelala.service.message.service.MqService;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private MqService mqService;
	@Autowired
	private ZkUtil zkUtil;
	
	@Value("${msg.msgExpire}")
	private int msgExpire;//消息处理超时时长(分钟)
	@Value("${msg.maxResendTimes}")
	private int maxResendTimes;
	@Value("${zk.expire}")
	private long zkExpire;//分布式锁加锁超时时长（分钟）
	
	@Override
	@Transactional
	public BaseResponse readySendMessage(MessageModel messageModel) {
		if(messageModel == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		//保存消息
		messageModel.setCreateTime(new Date());
		messageModel.setMessageStatus(MessageStatus.READY_SEND);
		messageModel.setResendTimes(0);
		messageModel.setExpireSenconds(msgExpire * 60);
		messageModel.setMaxSendTimes(maxResendTimes);
		int add = messageMapper.add(messageModel);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	@Transactional
	public BaseResponse sendingMessage(String messageId) {
		if(StringUtils.isEmpty(messageId)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		//更新状态
		MessageModel messageModel = messageMapper.findById(messageId);
		if(messageModel == null) {
			return new BaseResponse(MessageCode.MESSAGE_NOT_EXISTS,MessageMsg.MESSAGE_NOT_EXISTS);
		}
		messageModel.setMessageStatus(MessageStatus.SENDING);
		messageModel.setUpdateTime(new Date());
		messageModel.setMessageId(messageId);
		int updateStatus = messageMapper.updateStatus(messageModel);
		//用mq发送消息
		mqService.sendMessage(messageModel);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	@Transactional
	public BaseResponse sendedMessage(String messageId) {
		if(StringUtils.isEmpty(messageId)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		MessageModel messageModel = messageMapper.findById(messageId);
		String messageStatus = messageModel.getMessageStatus();
		if(!messageStatus.equals(MessageStatus.SENDING)) {
			return new BaseResponse(MessageCode.MESSAGE_STATUS_ERROR,MessageMsg.MESSAGE_STATUS_ERROR);
		}
		messageModel.setUpdateTime(new Date());
		messageModel.setMessageStatus(MessageStatus.SENDED);
		messageMapper.updateStatus(messageModel);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	public BaseResponse resendMessage(String messageId) {
		if(StringUtils.isEmpty(messageId)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		//加锁
		String path = "/" + messageId;
		long currentTimeMillis = System.currentTimeMillis();
		boolean createNode = zkUtil.createNode(path, String.valueOf(currentTimeMillis), zkExpire * 60 * 1000);
		if(createNode) {
			MessageModel messageModel = messageMapper.findById(messageId);
			if(messageModel == null) {
				return new BaseResponse(MessageCode.MESSAGE_NOT_EXISTS, MessageMsg.MESSAGE_NOT_EXISTS);
			}
			//状态已处理的消息不能在进行重发
			String messageStatus = messageModel.getMessageStatus();
			if(MessageStatus.SENDED.equals(messageStatus)) {
				return new BaseResponse(MessageCode.MESSAGE_STATUS_ERROR,MessageMsg.MESSAGE_STATUS_ERROR);
			}
			//判断是否超过最大重发次数
			int resendTimes = messageModel.getResendTimes();
			int maxSendTimes = messageModel.getMaxSendTimes();
			if(resendTimes > maxSendTimes) {
				return new BaseResponse(MessageCode.RESENDTIMES_BIGGER_THAN_MAX,MessageMsg.RESENDTIMES_BIGGER_THAN_MAX);
			}
			//更新重发次数和消息状态,发送消息到mq
			messageModel.setMessageStatus(MessageStatus.SENDING);
			messageModel.setResendTimes(resendTimes + 1);
			messageModel.setUpdateTime(new Date());
			messageMapper.updateStatus(messageModel);
			BaseResponse sendMessage = mqService.sendMessage(messageModel);
			if(sendMessage.getCode() != Code.SUCCESS) {
				throw new MessageException(MessageMsg.SYSTEM_ERROR);
			}
			zkUtil.deleteNode(path);
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//分页
	@Override
	public List<MessageModel> queryByQueueAndStatus(QueryByQueueAndStatusRequest request) {
		if(request == null) {
			return null;
		}
		int start = (request.getCurrentPage() - 1) * request.getPageSize();
		return messageMapper.queryByQueueAndStatus(request.getQueue(), request.getStatus(), start, request.getPageSize());
	}

	@Override
	@Transactional
	public BaseResponse deleteMessageById(String messageId) {
		if(StringUtils.isEmpty(messageId)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		messageMapper.delById(messageId);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

}
