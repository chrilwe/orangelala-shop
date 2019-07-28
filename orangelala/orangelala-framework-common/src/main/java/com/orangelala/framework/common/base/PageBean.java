package com.orangelala.framework.common.base;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageBean {
	private int totalPage;
	private List list;
	private int pageSize;
}
