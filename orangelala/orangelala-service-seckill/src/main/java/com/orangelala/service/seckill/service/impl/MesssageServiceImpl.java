package com.orangelala.service.seckill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.orangelala.service.seckill.service.MessageService;
import com.rabbitmq.client.Channel;

@Service
public class MesssageServiceImpl implements MessageService {
	
	@Autowired
	private Channel channel;
	
	@Value("${}")
	private String exchange;
	@Value("${}")
	private String queues;

	@Override
	public void sendMessage(String message) throws Exception {
		//路由key为exchange+queue,因为交换机会将消息路由到所有的队列中，所以queue随便取一个即可
		String[] split = queues.split(",");
		channel.basicPublish(exchange, exchange+"."+split[0], null, message.getBytes());
	}

	@Override
	public String receiveMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
