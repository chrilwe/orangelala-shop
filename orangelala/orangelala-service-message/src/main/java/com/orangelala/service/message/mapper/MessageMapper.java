package com.orangelala.service.message.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.orangelala.framework.model.message.MessageModel;
import com.orangelala.service.message.mapper.sql.Sql;

public interface MessageMapper {
	@Insert(value = Sql.addMessage)
	public int add(MessageModel messageModel);

	@Delete(value = Sql.delById)
	public int delById(String messageId);

	@Update(value = Sql.updateStatus)
	public int updateStatus(MessageModel messageModel);

	@Select(value = Sql.findById)
	public MessageModel findById(String messageId);

	@Select(Sql.queryByQueueAndStatus)
	public List<MessageModel> queryByQueueAndStatus(@Param("queue") String queue, @Param("status") String status,
			@Param("start") int start, @Param("end") int end);
	
}
