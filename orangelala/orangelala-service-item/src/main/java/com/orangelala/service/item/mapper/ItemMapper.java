package com.orangelala.service.item.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.orangelala.framework.model.item.Item;
import com.orangelala.service.item.mapper.sql.ItemSqlString;

public interface ItemMapper {
	@Insert(ItemSqlString.add)
	public int add(Item item);
	
	@Delete(ItemSqlString.delete)
	public int delete(String id);
	
	@Update(ItemSqlString.update)
	public int update(Item item);
	
	@Select(ItemSqlString.findById)
	public Item findById(String id);
}
