package com.orangelala.service.order.listener.process;
/**
 * 处理超时未成功的分布式事务消息
 * @author chrilwe
 *
 */
public interface MessageStatusExceptionProcessor {
	//处理准备发送状态超时的消息
	public void handleReadySendTimeoutMessage();
	//处理发送中状态超时的消息
	public void handleSendingTimeoutMessage();
}
