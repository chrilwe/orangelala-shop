package com.orangelala.service.message;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class TestRabbitMq {
	
	public static void main(String[] args) {
		//sender
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			connectionFactory.setHost("127.0.0.1");
			connectionFactory.setPort(5672);
			connectionFactory.setUsername("guest");
			connectionFactory.setPassword("guest");
			connectionFactory.setVirtualHost("/");
			connection = connectionFactory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare("exchange", BuiltinExchangeType.TOPIC);
			channel.queueBind("queue1","exchange","inform.#.key1.#");
			channel.queueBind("queue2","exchange","inform.#.key2.#");
			channel.queueDeclare("queue1", true, false, false, null);
			channel.queueDeclare("queue2", true, false, false, null);
			channel.basicPublish("exchange", "inform.key1.key2", null, "hello".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				channel.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		
		//consumer
		/*try {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			connectionFactory.setHost("127.0.0.1");
			connectionFactory.setPort(5672);
			connectionFactory.setUsername("guest");
			connectionFactory.setPassword("guest");
			connectionFactory.setVirtualHost("/");
			Connection connection = connectionFactory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare("exchange", BuiltinExchangeType.TOPIC);
			channel.queueDeclare("queue1", true, false, false, null);
			channel.queueDeclare("queue2", true, false, false, null);
			channel.queueBind("queue1","exchange","inform.#.key1.#");
			channel.queueBind("queue2","exchange","inform.#.key2.#");
			channel.basicConsume("queue2", new Consumer(){

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
				public void handleDelivery(String arg0, Envelope arg1, BasicProperties arg2, byte[] arg3)
						throws IOException {
					// TODO Auto-generated method stub
					System.out.println(arg0);
					String message = new String(arg3);
					System.out.println(message);
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
		}*/
	}
}
