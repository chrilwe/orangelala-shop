package com.orangelala.framework.common.item.inventory.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class QueryItemNumRequest extends BaseRequest {
	private String itemId;
}
