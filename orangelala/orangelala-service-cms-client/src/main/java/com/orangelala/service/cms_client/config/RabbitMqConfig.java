package com.orangelala.service.cms_client.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {
	//交换机名称
	public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
	//队列名称
	public static final String QUEUE_INFORM_CMS = "queue_inform_cms";
	//绑定交换机
	public static final String BINDING_QUEUE_INFORM_CNS = "binding_queue_inform_cms";
	
	/**
	 * 交换机配置
	 */
	@Bean(EXCHANGE_TOPICS_INFORM)
	public Exchange EXCHANGE_TOPICS_INFORM() {
		//持久化交换机，重启后交换机还是存在的
		return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
	}
	
	/**
	 * 队列配置
	 */
	@Bean(QUEUE_INFORM_CMS)
	public Queue QUEUE_INFORM_CMS() {
		Queue queue = new Queue(QUEUE_INFORM_CMS);
		return queue;
	}
	
	/**
	 * 将队列绑定到交换机
	 */
	@Bean
	public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_CMS)Queue queue,
			@Qualifier(EXCHANGE_TOPICS_INFORM)Exchange exchange) {
		
		return BindingBuilder.bind(queue).to(exchange).with("inform.#.cms.#").noargs();
	}
	
}
