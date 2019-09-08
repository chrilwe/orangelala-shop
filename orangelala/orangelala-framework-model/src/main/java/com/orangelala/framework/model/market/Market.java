package com.orangelala.framework.model.market;

import lombok.Data;
import lombok.ToString;

/**
 * 店铺商品营销信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class Market {
	//店铺号
	private String shopId;
	//商品id
	private String itemId;
	//日销量
	private long daySales;
	//月销量
	private long monthSales;
	//年销量
	private long yearSales;
	//累计销量
	private long totalSales;
}
