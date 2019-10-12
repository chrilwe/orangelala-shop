package com.orangelala.framework.common.search.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class SearchRequest extends BaseRequest {
	private String keyword;
	private String priceSort;//价格排序
	private String sellNumSort;//销量排序
	private String evaluateSort;//评价排序
	private String firstGrideCate;//一级分类
	private String secondGrideCate;//二级分类
	private String thirdGrideCate;//三级分类
}
