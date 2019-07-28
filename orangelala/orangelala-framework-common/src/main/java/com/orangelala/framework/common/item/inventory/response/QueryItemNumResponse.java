package com.orangelala.framework.common.item.inventory.response;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.item.inventory.ItemInventory;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class QueryItemNumResponse extends BaseResponse {
	private String itemId;
	private int num;
	public QueryItemNumResponse(int code, String msg, String itemId, int num) {
		super(code, msg);
		this.itemId = itemId;
		this.num = num;
	}

}
