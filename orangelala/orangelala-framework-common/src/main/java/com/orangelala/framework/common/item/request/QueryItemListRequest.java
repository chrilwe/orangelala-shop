package com.orangelala.framework.common.item.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class QueryItemListRequest extends BaseRequest {
	private int page;
	private int pageSize;
	private String staus;//正常、下架，删除
	private String keyword;//模糊查询关键字
}
