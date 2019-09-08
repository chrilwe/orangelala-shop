package com.orangelala.service.ucenter.service;

import java.util.List;

import com.orangelala.framework.model.ucenter.Region;

public interface RegionService {
	//根据区域级别查询区域 
	public List<Region> findRegionByLevel(int level);
	
	//根据区域pid查询当前级别下的区域
	public List<Region> findRegionBypid(int pid);
	
	//根据级别和名称模糊查询
	public List<Region> findRegionByLevelAndLikeName(int level,String keyword);
	
	//根据id查询
	public Region findRegionById(int id);
}
