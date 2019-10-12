package com.orangelala.framework.common.search.response;

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.item.ItemInfo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SearchResult extends BaseResponse {
	
	private List<ItemInfo> itemList;
	private int totalCount;
	private int totalPage;
	private int pageSize;
	
	public SearchResult(int code, String msg) {
		super(code, msg);
	}

}
