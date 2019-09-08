package com.orangelala.framework.model.item.price;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 商品价格
 * @author chrilwe
 *
 */
@Data
@ToString
public class ItemPrice {
	
	private long price;//现价
	private long originPrice;//原价
	private String itemId;
	private Date createTime;
	private Date updateTime;
	private int version;
	private int isUsedWithOther;//是否与其它优惠活动叠加
	private int num;//优惠件数
}
