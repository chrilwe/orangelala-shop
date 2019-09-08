package com.orangelala.service.order.listener;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 分布式事务监听
 * @author chrilwe
 *
 */
@Component
public class DistributeTransactionListener {
	
	@Value("${rabbitmq.transaction.exchange}")
	private String exchange;
	@Value("${rabbitmq.transaction.queue}")
	private String queue;
	@Value("${rabbitmq.transaction.queueSize}")
	private int queueSize;
	@Value("${rabbitmq.host}")
	private String host;
	@Value("${rabbitmq.post}")
	private int port;
	@Value("${rabbitmq.username}")
	private String username;
	@Value("${rabbitmq.password}")
	private String password;
	@Value("${rabbitmq.virtualHost}")
	private String virtualHost;
	
	//spring容器启动实例化队列对应数量的rabbitmqClient监听实例
	public void startListener() {
		String[] processTypes = queue.split(",");
		for(int i = 0;i < processTypes.length * queueSize;i++) {
			try {
				ConnectionFactory connectionFactory = new ConnectionFactory();
				connectionFactory.setHost(host);
				connectionFactory.setPort(port);
				connectionFactory.setUsername(username);
				connectionFactory.setPassword(password);
				connectionFactory.setVirtualHost(virtualHost);
				Connection connection = connectionFactory.newConnection();
				Channel channel = connection.createChannel();
				channel.exchangeDeclare("exchange", BuiltinExchangeType.TOPIC);
				List<String> list = new ArrayList<String>();
				for(String queue : processTypes) {
					String queueName1 = queue + "1";
					String queueName2 = queue + "2";
					list.add(queueName1);
					list.add(queueName2);
				}
				for(String queueName: list) {
					channel.queueDeclare(queueName, true, false, false, null);
					channel.queueBind(queueName,exchange,"inform.#."+queueName+".#");
				}
				String consumeQueueName = processTypes[(i+1)%queueSize] + ((i+1)%queueSize+1);
				channel.basicConsume(consumeQueueName, new Consumer(){

					@Override
					public void handleCancel(String arg0) throws IOException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void handleCancelOk(String arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void handleConsumeOk(String arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void handleDelivery(String arg0, Envelope arg1, BasicProperties arg2, byte[] message)
							throws IOException {
						String msg = new String(message);
						//根据队列名称获取处理类名
						int length = consumeQueueName.length();
						String objectName = consumeQueueName.substring(0,length-1);
						String fullObjectName = "com.orangelala.service.order.listener.process.impl." + objectName;
						
						//根据类名获取执行业务的对象
						try {
							Class<?> objectClass = Class.forName(fullObjectName);
							Method method = objectClass.getMethod("process",String.class);
							method.invoke(objectClass.newInstance(), msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void handleRecoverOk(String arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void handleShutdownSignal(String arg0, ShutdownSignalException arg1) {
						// TODO Auto-generated method stub
						
					}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
