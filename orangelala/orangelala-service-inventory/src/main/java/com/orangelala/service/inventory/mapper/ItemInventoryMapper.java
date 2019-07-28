package com.orangelala.service.inventory.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.orangelala.framework.model.item.inventory.ItemInventory;
import com.orangelala.service.inventory.mapper.sql.SqlString;

public interface ItemInventoryMapper {
	//添加
	@Insert(SqlString.add)
	public int add(ItemInventory itemInventory);
	//更新
	@Update(SqlString.update)
	public int update(ItemInventory itemInventory);
	//查询
	@Select(SqlString.findByItemId)
	public ItemInventory findByItemId(String itemId);
}
