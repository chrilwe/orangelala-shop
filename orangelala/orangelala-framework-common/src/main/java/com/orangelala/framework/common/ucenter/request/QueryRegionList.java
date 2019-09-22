package com.orangelala.framework.common.ucenter.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class QueryRegionList extends BaseRequest {
	private int level;
	private int size;
	private int page;
	private String keyword;
}
