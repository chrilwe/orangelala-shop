package com.orangelala.framework.api;
/**
 * 搜索服务管理
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.search.request.SearchRequest;
import com.orangelala.framework.common.search.response.SearchResult;

public interface ISearchController {
	//搜索商品
	public SearchResult search(SearchRequest searchRequest);
}
