package com.orangelala.service.inventory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.orangelala.framework.model.item.inventory.ItemInventoryRecord;
import com.orangelala.service.inventory.mapper.sql.SqlString;

public interface ItemInventoryRecordMapper {
	@Insert(SqlString.addItemInventoryRecord)
	public int addItemInventoryRecord(ItemInventoryRecord itemInventoryRecord);
	
	@Select(SqlString.findItemInventoryRecordByOrderNum)
	public List<ItemInventoryRecord> findByOrderNumber(String orderNumber);
}
