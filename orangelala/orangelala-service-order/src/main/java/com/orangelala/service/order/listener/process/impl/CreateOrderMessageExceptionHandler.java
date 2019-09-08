package com.orangelala.service.order.listener.process.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.message.request.QueryByQueueAndStatusRequest;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.model.item.inventory.ItemInventoryRecord;
import com.orangelala.framework.model.message.MessageModel;
import com.orangelala.framework.model.message.status.MessageStatus;
import com.orangelala.framework.model.order.Order;
import com.orangelala.service.order.client.InventoryClient;
import com.orangelala.service.order.client.MessageClient;
import com.orangelala.service.order.listener.process.MessageStatusExceptionProcessor;
import com.orangelala.service.order.service.OrderService;

@Component
public class CreateOrderMessageExceptionHandler implements MessageStatusExceptionProcessor {
	private static final int pageSize = 10; //每次分页查询消息的数量
	private static final int currentPage = 1;//每次从第一页开始查询
	@Value("${rabbitmq.transaction.queue}")
	private String queue;
	@Autowired
	private MessageClient messageClient;
	@Autowired
	private InventoryClient inventoryClient;
	@Autowired
	private OrderService orderService;
	
	//处理准备发送消息超时
	@Override
	public void handleReadySendTimeoutMessage() {
		try {
			int currentSeconds = (int) (System.currentTimeMillis()/1000);
			List<MessageModel> list = this.query(MessageStatus.READY_SEND);
			for(MessageModel messageModel : list) {
				int expireSeconds = messageModel.getExpireSenconds();//过期时间（秒）
				Date createTime = messageModel.getCreateTime();
				if(currentSeconds - createTime.getTime() > expireSeconds) {
					String messageBody = messageModel.getMessageBody();
					OrderAddRequest orderAddRequest = JSON.parseObject(messageBody, OrderAddRequest.class);
					List<ItemInventoryRecord> l = inventoryClient.findItemInventoryRecordByOrderNum(orderAddRequest.getOrder().getOrderNumber());
					if(l == null) {
						messageClient.deleteMessageById(messageModel.getMessageId());
					} else {
						messageClient.resendMessage(messageModel.getMessageId());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//处理发送中消息超时
	@Override
	public void handleSendingTimeoutMessage() {
		int currentSeconds = (int) (System.currentTimeMillis()/1000);
		List<MessageModel> list = this.query(MessageStatus.SENDING);
		for (MessageModel messageModel : list) {
			int expireSeconds = messageModel.getExpireSenconds();//过期时间（秒）
			Date updateTime = messageModel.getUpdateTime();
			if(currentSeconds - updateTime.getTime() > expireSeconds) {
				messageClient.resendMessage(messageModel.getMessageId());
			}
		}
		List<MessageModel> list1 = this.query(MessageStatus.SENDED);
		for(MessageModel messageModel : list1) {
			String messageBody = messageModel.getMessageBody();
			OrderAddRequest orderAddRequest = JSON.parseObject(messageBody, OrderAddRequest.class);
			//查询订单是否已创建
			Order order = orderService.queryOrderById(orderAddRequest.getOrder().getOrderNumber());
			if(order == null) {
				
			}
			//查询优惠券是否已使用
		}
	}
	
	private List<MessageModel> query(String status) {
		//每次分页查询
		QueryByQueueAndStatusRequest request = new QueryByQueueAndStatusRequest();
		request.setCurrentPage(currentPage);
		request.setPageSize(pageSize);
		request.setStatus(status);
		request.setQueue(queue);
		return messageClient.queryByQueueAndStatus(request);
	}
}
