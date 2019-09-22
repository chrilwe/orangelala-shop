package com.orangelala.framework.model.item.inventory;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 商品库存
 * @author chrilwe
 *
 */
@Data
@ToString
public class ItemInventory {
	
	private String id;
	private int num;
	private String itemId;
	private Date createTime;
	private Date updateTime;
	private int version;
}
