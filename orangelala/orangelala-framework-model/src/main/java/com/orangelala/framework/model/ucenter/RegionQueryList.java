package com.orangelala.framework.model.ucenter;

import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * 分页查询返回参数
 * @author chrilwe
 *
 */
@Data
@ToString
public class RegionQueryList {
	private int totalPage;
	private int pageSize;
	private int total;
	private List<Region> regionList;
}
