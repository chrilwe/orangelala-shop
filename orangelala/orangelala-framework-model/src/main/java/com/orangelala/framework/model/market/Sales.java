package com.orangelala.framework.model.market;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 商品销量
 * @author chrilwe
 *
 */
@Data
@ToString
public class Sales {
	//商品id
	private String itemId;
	//店铺号
	private String shopId;
	//售出商品时间
	private Date saleTime;
	//购买者
	private String userId;
	//购买数量
	private String buyNum;
	//商品单价
	private long price;
	//商品原价
	private long originPrice;
}
