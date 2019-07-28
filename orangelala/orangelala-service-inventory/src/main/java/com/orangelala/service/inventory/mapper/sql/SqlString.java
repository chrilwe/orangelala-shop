package com.orangelala.service.inventory.mapper.sql;

public class SqlString {
	
	public static final String add = "insert into orangelala_item_inventory(id,num,item_id,create_time,update_time"
			+ ") values(#{id},#{num},#{itemIid},#{createTime},#{updateTime})";
	
	public static final String update = "update orangelala_item_inventory set num=#{num},update_time=#{updateTime}"
			+ " where item_id=#{itemId}";
	
	public static final String findByItemId = "select id,num,item_id as itemId,create_time as createTime,update_time as updateTime"
			+ " from orangelala_item_inventory where item_id=#{itemId}";
}
