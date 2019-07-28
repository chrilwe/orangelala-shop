package com.orangelala.framework.common.cms.response;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.PageBean;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class QueryPageListResponse extends BaseResponse {
	public QueryPageListResponse(int code, String msg, PageBean pageBean) {
		super(code, msg);
		this.pageBean = pageBean;
	}
	private PageBean pageBean;
}
