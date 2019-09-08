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
	private long originPrice;
	public QueryItemPriceResponse(int code, String msg, String itemId, long price, long originPrice) {
		super(code, msg);
		this.itemId = itemId;
		this.price = price;
		this.originPrice = originPrice;
	}

}
