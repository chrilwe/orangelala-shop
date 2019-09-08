package com.orangelala.service.order.listener.process.impl;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.model.message.MessageModel;
import com.orangelala.framework.utils.ZkUtil;
import com.orangelala.service.order.configs.SpringBeanGetter;
import com.orangelala.service.order.listener.process.MessageProcessor;
import com.orangelala.service.order.service.OrderService;
/**
 * 创建订单
 * @author chrilwe
 *
 */
public class CreateOrder implements MessageProcessor {

	@Override
	public void process(String message) {
		MessageModel messageModel = JSON.parseObject(message, MessageModel.class);
		String messageId = messageModel.getMessageId();
		String messageBody = messageModel.getMessageBody();
		OrderAddRequest orderAddRequest = JSON.parseObject(messageBody, OrderAddRequest.class);
		//多个消费者处理，只允许一个消费者成功
		SpringBeanGetter beanGetter = new SpringBeanGetter();
		ZkUtil zkUtil = beanGetter.getBean(ZkUtil.class);
		OrderService orderService = beanGetter.getBean(OrderService.class);
		String path = "/"+messageId;
		boolean createNode = zkUtil.createNode(path, "");
		if(createNode) {
			orderService.addOrder(orderAddRequest, ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest());
		}
		zkUtil.deleteNode(path);
	}

}
