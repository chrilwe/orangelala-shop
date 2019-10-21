package com.orangelala.service.seckill.service;
/**
 * 消息管理
 * @author crilwe
 *
 */
public interface MessageService {
	public void sendMessage(String message) throws Exception;
	public String receiveMessage();
}
