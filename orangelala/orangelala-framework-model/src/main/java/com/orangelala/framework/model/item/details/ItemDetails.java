package com.orangelala.framework.model.item.details;

import lombok.Data;
import lombok.ToString;

/**
 * 商品细节
 * @author chrilwe
 *
 */
@Data
@ToString
public class ItemDetails {
	private String id;
	private String itemId;
	private String desc;
}
