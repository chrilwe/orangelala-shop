package com.orangelala.service.message.mapper.sql;

public class Sql {
	public static final String findById = "select message_id as messageId,message_body as messageBody,message_status as messageStatus,data_type as dataType,exchange,queue,routing_key as routingKey,create_time as createTime,update_time as createTime,max_send_times as maxSendTimes,expire_senconds as expireSenconds,resend_times as resendTimes from orangelala_message where message_id=#{messageId}";
	public static final String addMessage = "insert into orangelala(message_id,message_body,message_status,data_type,exchange,queue,routing_key,create_time,update_time,max_send_times,expire_senconds,resend_times) values(#{messageId},#{messageBody},#{messageStatus},#{dataType},#{exchange},#{queue},#{routingKey},#{createTime},#{createTime},#{maxSendTimes},#{expireSenconds},#{resendTimes})";
	public static final String updateStatus = "update orangelala_message set message_status=#{messageStatus},update_time=#{updateTime},resend_times=#{resendTimes} where message_id=#{messageId}";
	public static final String delById = "delete from orangelala_message where message_id=#{messageId}";
	public static final String queryByQueueAndStatus = "select message_id as messageId,message_body as messageBody,message_status as messageStatus,data_type as dataType,exchange,queue,routing_key as routingKey,create_time as createTime,update_time as createTime,max_send_times as maxSendTimes,expire_senconds as expireSenconds,resend_times as resendTimes from orangelala_message where queue=#{queue} and status=#{status} limit #{start},#{end}";
}
