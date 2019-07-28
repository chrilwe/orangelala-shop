package com.orangelala.framework.common.shop.request;


import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class QueryShopsRequest extends BaseRequest {
	private String status;
	private String keyword;
	private int page;
	private int pageSize;
}
