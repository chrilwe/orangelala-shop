package com.orangelala.framework.common.item.response;

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.PageBean;
import com.orangelala.framework.model.item.Item;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class QueryItemListResponse extends BaseResponse {
	PageBean pageBean;
	public QueryItemListResponse(int code, String msg, PageBean pageBean) {
		super(code, msg);
		this.pageBean = pageBean;
	}
	
}
