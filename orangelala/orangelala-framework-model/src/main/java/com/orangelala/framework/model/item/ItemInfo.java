package com.orangelala.framework.model.item;

import lombok.Data;
import lombok.ToString;

/**
 * 商品详细信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class ItemInfo extends Item {
	private long price;
	private int num;
}
