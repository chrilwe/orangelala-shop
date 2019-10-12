package com.orangelala.service.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.ISearchController;
import com.orangelala.framework.common.search.request.SearchRequest;
import com.orangelala.framework.common.search.response.SearchResult;

@RestController
@RequestMapping("/search")
public class SearchController implements ISearchController {
	
	//搜索商品
	@Override
	@GetMapping("/itemInfo")
	public SearchResult search(SearchRequest searchRequest) {
		
		return null;
	}

}
