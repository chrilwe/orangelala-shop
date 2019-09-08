package com.orangelala.service.message.service.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.message.exception.MessageException;
import com.orangelala.framework.common.message.response.code.MessageCode;
import com.orangelala.framework.common.message.response.msg.MessageMsg;
import com.orangelala.framework.model.message.MessageModel;
import com.orangelala.service.message.service.MqService;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * rabbitmq消息发送
 * @author chrilwe
 *
 */
@Service
public class MqServiceImpl implements MqService {
	
	@Value("${spring.rabbitmq.host}")
	private String mqHost;
	@Value("${spring.rabbitmq.port}")
	private int mqPort;
	@Value("${spring.rabbitmq.username}")
	private String mqUsername;
	@Value("${spring.rabbitmq.password}")
	private String mqPassword;
	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualHost;

	@Override
	public BaseResponse sendMessage(MessageModel messageModel) {
		if(messageModel == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		String exchange = messageModel.getExchange();
		String queue = messageModel.getQueue();
		String routingKey = messageModel.getRoutingKey();
		String messageBody = messageModel.getMessageBody();
		if(StringUtils.isNoneEmpty(exchange,queue,routingKey,messageBody)) {
			return new BaseResponse(MessageCode.ANY_PARAM_IS_NULL,MessageMsg.ANY_PARAM_IS_NULL);
		}
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			connectionFactory.setHost(mqHost);
			connectionFactory.setPort(mqPort);
			connectionFactory.setUsername(mqUsername);
			connectionFactory.setPassword(mqPassword);
			connectionFactory.setVirtualHost(virtualHost);
			
			connection = connectionFactory.newConnection();
			//创建与交换机交流的通道
			channel = connection.createChannel();
			channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
			String[] queueArr = queue.split(",");
			String[] routingKeyArr = routingKey.split(",");//routingKey "inform.#.key.#"
			if(queueArr.length != routingKeyArr.length) {
				throw new MessageException(MessageMsg.ROUTINGKEY_OR_QUEUENAME_ERROR);
			}
			boolean checkKey = this.checkKey(routingKey);
			if(!checkKey) {
				throw new MessageException(MessageMsg.ROUTINGKEY_ERROR);
			}
			String key = "";
			for(int i = 0;i < queueArr.length;i++) {
				channel.queueDeclare(queueArr[i], true, false, false, null);
				channel.queueBind(queueArr[i],exchange,routingKeyArr[i]);
				String[] split = routingKeyArr[i].split(".");
				if(i == queueArr.length - 1) {
					key += split[2];
				}
				key += split[2] + ".";
			}
			
			channel.basicPublish(exchange, "inform."+key, null, messageBody.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		} finally {
			if(channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//校验RoutingKey是否符合规范 "inform.#.key.#"
	private boolean checkKey(String key) {
		String[] split = key.split(",");
		for(String k : split) {
			String[] split2 = k.split(".");
			if(split2.length != 4) {
				return false;
			}
			if(!split2[1].equals("#") || !split2[3].equals("#")) {
				return false;
			}
		}
		return true;
	}
}
