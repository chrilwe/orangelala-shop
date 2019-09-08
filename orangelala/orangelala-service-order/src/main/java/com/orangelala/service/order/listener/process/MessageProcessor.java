package com.orangelala.service.order.listener.process;
/**
 * 处理分布式事务消息业务
 * @author chrilwe
 *
 */
public interface MessageProcessor {
	public void process(String message);
}
