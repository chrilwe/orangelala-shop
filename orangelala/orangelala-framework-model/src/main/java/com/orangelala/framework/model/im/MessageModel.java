package com.orangelala.framework.model.im;
/**
 * IM消息模型
 * @author chrilwe
 *
 */

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageModel extends ConnectionModel implements Serializable {
	//发送者
	private String sender;
	//接收者
	private String receiver;
	//消息体
	private String message;
	//消息类型 ：群发，私聊
	private String messageType;
}
