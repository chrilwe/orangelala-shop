package com.orangelala.service.inventory.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.inventory.exception.ItemInventoryException;
import com.orangelala.framework.common.item.inventory.request.QueryItemNumRequest;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.inventory.response.QueryItemNumResponse;
import com.orangelala.framework.common.item.inventory.response.ReduceInventoryResponse;
import com.orangelala.framework.common.item.inventory.response.code.InventoryCode;
import com.orangelala.framework.common.item.inventory.response.msg.InventoryMsg;
import com.orangelala.framework.common.item.response.code.ItemCode;
import com.orangelala.framework.common.item.response.msg.ItemMsg;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.item.inventory.ItemInventory;
import com.orangelala.framework.model.item.inventory.ItemInventoryRecord;
import com.orangelala.framework.utils.ZkUtil;
import com.orangelala.service.inventory.client.ItemClient;
import com.orangelala.service.inventory.mapper.ItemInventoryMapper;

@Service
public class ItemInventoryService {
	private static final String ITEM_NUM = "itemNum_";
	@Autowired
	private ItemInventoryMapper itemInventoryMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ItemClient itemClient;

	// 查询商品库存
	public QueryItemNumResponse queryItemNum(QueryItemNumRequest request) {
		if (request == null) {
			return new QueryItemNumResponse(Code.PARAM_NULL, Msg.PARAM_NULL, "", 0);
		}
		String itemId = request.getItemId();
		// 从redis中查询库存
		String itemNumStr = stringRedisTemplate.boundValueOps(ITEM_NUM + itemId).get();
		int itemNum = 0;
		if (StringUtils.isEmpty(itemNumStr)) {
			// redis没有获取到库存，从数据库中查询
			ItemInventory itemInventory = itemInventoryMapper.findByItemId(itemId);
			itemNum = itemInventory.getNum();
			stringRedisTemplate.boundValueOps(ITEM_NUM+itemId).set(itemNum+"");
		} else {
			itemNum = Integer.parseInt(itemNumStr);
		}
		return new QueryItemNumResponse(Code.SUCCESS, Msg.SUCCESS, itemId, itemNum);
	}
	
	
	//减少库存
	//map--> key: itemId  value: num
	public ReduceInventoryResponse reduce(Map<String, Integer> map) {
		if(map == null) {
			return new ReduceInventoryResponse(Code.PARAM_NULL,Msg.PARAM_NULL,null);
		}
		//删除redis库存
		List<String> itemIds = new ArrayList<String>();
		for(Iterator iterator = map.entrySet().iterator();iterator.hasNext();) {
			String key = (String) iterator.next();
			stringRedisTemplate.delete(ITEM_NUM+key);
			itemIds.add(key);
		}
		
		//查询库存是否充足
		List<ItemInventory> list = itemInventoryMapper.findByItemIds(itemIds);
		if(list == null) {
			return new ReduceInventoryResponse(InventoryCode.ITEM_NUM_NULL,InventoryMsg.ITEM_NUM_NULL,null);
		}
		List<String> list1 = new ArrayList<String>();
		for(ItemInventory itemInventory : list) {
			int num = itemInventory.getNum();
			String itemId = itemInventory.getItemId();
			//库存不足
			if(num < map.get(itemId)) {
				list1.add(itemId);
			}
		}
		
		//批量更新库存
		if(list1 == null || list1.size() <= 0) {
			int reduce = itemInventoryMapper.reduce(map);
			if(reduce <= 0) {
				//更新失败，重新更新，直到限制次数
				for(int i = 0;i < 5;i++) {
					ReduceInventoryResponse res = reduce(map);
					if(res.getCode() == Code.SUCCESS) {
						break;
					}
				}
			}
		} else {
			return new ReduceInventoryResponse(InventoryCode.ITEM_INVENTORY_NO_ENOUGH,InventoryMsg.ITEM_INVENTORY_NO_ENOUGH,list1);
		}
		return new ReduceInventoryResponse(Code.SUCCESS,Msg.SUCCESS,null);
	}
	
	//添加库存
	public BaseResponse increase(String itemId,int num) {
		if(StringUtils.isEmpty(itemId) || num <= 0) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		ItemInventory itemInventory = itemInventoryMapper.findByItemId(itemId);
		//存在，添加库存，不存在，创建库存
		boolean flag = false;
		if(itemInventory != null) {
			for(int i = 0;i < 5;i++) {
				int increase = itemInventoryMapper.increase(itemId, num, itemInventory.getVersion());
				if(increase > 0) {
					flag = true;
					break;
				}
			}
			if(!flag) {
				return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
			}
		} else {
			ItemInfo itemInfo = itemClient.findItemInfoById(itemId);
			if(itemInfo == null) {
				return new BaseResponse(ItemCode.ITEM_NO_EXISTS,ItemMsg.ITEM_NO_EXISTS);
			}
			ItemInventory data = new ItemInventory();
			data.setCreateTime(new Date());
			data.setItemId(itemId);
			data.setVersion(1);
			ItemInventory insert = itemInventoryMapper.insert(data);
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
}
