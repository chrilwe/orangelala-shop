package com.orangelala.service.inventory.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.orangelala.framework.model.item.inventory.ItemInventory;

public interface ItemInventoryMapper {
	//插入
	public ItemInventory insert(ItemInventory itemInventory);
	//查询
	public ItemInventory findByItemId(String itemId);
	//批量减少库存 map-> key:itemId value:num
	public int reduce(Map<String,Integer> map);
	//批量查询库存
	public List<ItemInventory> findByItemIds(List<String> itemIds);
	//添加库存
	public int increase(@Param("itemId")String itemId,@Param("num")int num,@Param("version")int version);
}
