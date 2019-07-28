package com.orangelala.framework.common.cms.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

/**
 * 页面列表
 * @author chrilwe
 *
 */
@Data
public class QueryPageListRequest extends BaseRequest {
	private String pageName;
	private String pageStatus;
	private int page;
	private int pageSize;
}
