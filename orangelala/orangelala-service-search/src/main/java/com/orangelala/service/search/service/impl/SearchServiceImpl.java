package com.orangelala.service.search.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.springframework.stereotype.Service;

import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.service.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
	
	//创建文档
	@Override
	public void createDoc(ItemInfo itemInfo) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("user", "kimchy");
		jsonMap.put("postDate", new Date());
		jsonMap.put("message", "trying out Elasticsearch");
		IndexRequest indexRequest = new IndexRequest("");
	}

	@Override
	public void createIndex(String index) {
		// TODO Auto-generated method stub
		
	}

}
