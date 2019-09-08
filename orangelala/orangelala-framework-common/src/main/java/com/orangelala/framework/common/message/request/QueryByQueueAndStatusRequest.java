package com.orangelala.framework.common.message.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class QueryByQueueAndStatusRequest extends BaseRequest {
	private String queue;
	private String status;
	private int currentPage;
	private int pageSize;
}
