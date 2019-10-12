package com.orangelala.service.search.service;

import com.orangelala.framework.model.item.ItemInfo;

public interface SearchService {
	public void createDoc(ItemInfo itemInfo);
	
	public void createIndex(String index);
}
