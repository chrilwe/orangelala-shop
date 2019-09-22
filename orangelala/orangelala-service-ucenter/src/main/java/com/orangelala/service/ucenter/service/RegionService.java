package com.orangelala.service.ucenter.service;

import java.util.List;

import com.orangelala.framework.common.ucenter.request.QueryRegionList;
import com.orangelala.framework.model.ucenter.Region;
import com.orangelala.framework.model.ucenter.RegionQueryList;
import com.orangelala.framework.model.ucenter.RegionTreeNode;

public interface RegionService {
	//根据区域级别查询区域 
	public List<Region> findRegionByLevel(int level);
	
	//根据区域pid查询当前级别下的区域
	public List<Region> findRegionBypid(int pid);
	
	//根据id查询
	public Region findRegionById(int id);
	
	//树状结构数据
	public RegionTreeNode regionTreeNode();
	
	//条件分页查询
	public RegionQueryList queryRegionList(QueryRegionList queryRequest);
	
	//批量查询
	public List<Region> queryRegionBatch(String ids);
	
	//批量更新
	public void updateRegionBatch(List<Region> list);
	
	//更新
	public void updateRegion(Region region);
	
	//查询所有
	public List<Region> findAllRegion();
	
}
