package com.orangelala.framework.common.base;

import java.util.List;

import lombok.Data;
import lombok.ToString;

public class PageBean {
	private int totalPage;
	private List list;
	private int pageSize;
	private int totalCount;
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
	}
	
}
