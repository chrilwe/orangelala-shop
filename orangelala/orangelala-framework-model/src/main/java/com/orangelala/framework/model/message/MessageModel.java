package com.orangelala.framework.model.message;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageModel {
	private String messageId;//消息id
	private String messageBody;//消息内容
	private String messageStatus;//消息状态
	private String dataType;//消息内容数据类型:json,string
	private String exchange;//交换机
	private String queue;//队列
	private String routingKey;//路由key
	private Date createTime;//消息生成时间
	private Date updateTime;//消息更新时间
	private int maxSendTimes;//消息最大重发数
	private int expireSenconds;//消息超时时间(秒)
	private int resendTimes;//当前重发次数
}
