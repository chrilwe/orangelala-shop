package com.orangelala.framework.common.item.inventory.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class UpdateItemNumRequest extends BaseRequest {
	private int buyItemNum;//购买数量
	private String itemId;
}
