package com.orangelala.service.seckill.configs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.utils.ZkUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class SeckillConfiguration {
	
	String mqHost;
	int mqPort;
	String mqUsername;
	String mqPassword;
	String virtualHost;
	String exchange;
	String queues;
	String zkNodes;
	
	//实例化一个rabbitmq连接客户端
	@Bean
	public Channel channel() throws Exception {
		//校验参数
		if(StringUtils.isAnyEmpty(mqHost,mqUsername,mqPassword,virtualHost,exchange,queues)) {
			throw new RuntimeException(Msg.PARAM_NULL);
		}
		
		//初始化连接factory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(mqHost);
		connectionFactory.setPort(mqPort);
		connectionFactory.setUsername(mqUsername);
		connectionFactory.setPassword(mqPassword);
		connectionFactory.setVirtualHost(virtualHost);
		
		//解析queues
		String[] queuesArray = queues.split(",");
		
		//建立连接，绑定交换机和队列
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		for(String queue : queuesArray) {
			channel.queueDeclare(queue, true, false, false, null);
			channel.queueBind(queue,exchange,exchange+".#"+queue+".#");
		}
		channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
		return channel;
	}
	
	//实例化一个zk客户端连接
	@Bean
	public ZkUtil zkUtil() {
		return new ZkUtil(zkNodes);
	}
}
