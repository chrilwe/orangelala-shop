package com.orangelala.framework.common.item.price.response;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.item.price.ItemPrice;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class QueryItemPriceResponse extends BaseResponse {
	private String itemId;
	private long price;
	public QueryItemPriceResponse(int code, String msg, String itemId, long price) {
		super(code, msg);
		this.itemId = itemId;
		this.price = price;
	}

}
