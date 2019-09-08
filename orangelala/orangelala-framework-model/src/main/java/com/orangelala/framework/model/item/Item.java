package com.orangelala.framework.model.item;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 商品基本信息
 * 
 * @author chrilwe
 *
 */
@Data
@ToString
public class Item {
	private String id;
	
	private String shopId;
	
	private String title;

	private String sellPoint;

	private String barcode;

	private String image;

	private Long cid;
	
	private String type;//属性
	
	private String packages;

	private String status;

	private Date created;

	private Date updated;
	
	private int freeShipping;//是否包邮：1.包邮 0.不包邮
}
