package com.orangelala.framework.model.shop;

import lombok.Data;
import lombok.ToString;

/**
 * 促销活动详情
 * @author chrilwe
 *
 */
@Data
@ToString
public class ActivityDetail {
	private int id;//主键自增
	private int activityId;
	private int payMoney;//满减金额
	private int price;//减少金额
	private int status;//活动状态
}
