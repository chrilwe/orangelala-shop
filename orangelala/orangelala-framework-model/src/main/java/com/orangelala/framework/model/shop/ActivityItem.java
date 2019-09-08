package com.orangelala.framework.model.shop;

import lombok.Data;
import lombok.ToString;

/**
 * 参与商铺活动的商品
 * @author chrilwe
 *
 */
@Data
@ToString
public class ActivityItem {
	private String itemId;
	private int activityId;
}
