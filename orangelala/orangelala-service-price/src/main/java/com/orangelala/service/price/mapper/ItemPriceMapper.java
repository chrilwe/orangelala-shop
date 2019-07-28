package com.orangelala.service.price.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.orangelala.framework.model.item.price.ItemPrice;
import com.orangelala.service.price.mapper.sql.SqlString;

public interface ItemPriceMapper {
	@Insert(SqlString.add)
	public int add(ItemPrice itemPrice);
	
	@Delete(SqlString.delete)
	public int delete(String id);
	
	@Update(SqlString.update)
	public int update(ItemPrice itemPrice);
	
	@Select(SqlString.findByItemId)
	public ItemPrice findByItemId(String itemId);
}
