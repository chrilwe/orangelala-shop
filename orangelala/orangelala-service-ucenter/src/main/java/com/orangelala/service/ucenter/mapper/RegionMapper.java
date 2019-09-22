package com.orangelala.service.ucenter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.orangelala.framework.model.ucenter.Region;
import com.orangelala.framework.model.ucenter.RegionQueryList;
import com.orangelala.framework.model.ucenter.RegionTreeNode;

public interface RegionMapper {
	public List<Region> findRegionByLevel(int level);
	
	public List<Region> findRegionBypid(int pid);

	public Region findRegionById(int id);
	
	public RegionTreeNode queryRegionTreeNode();
	
	public RegionQueryList queryRegionListBylevelAndKeyword(@Param("level")int level,@Param("name")String name,@Param("start")int start,@Param("size")int size);

	public RegionQueryList queryRegionListByKeyword(@Param("name")String name,@Param("start")int start,@Param("size")int size);

	public RegionQueryList queryRegionListByLevel(@Param("level")int level,@Param("start")int start,@Param("size")int size);

	public RegionQueryList queryRegionList(@Param("start")int start,@Param("size")int size);

	public List<Region> queryRegionBatcByIdList(List<Integer> list);
	
	public void updateRegion(Region region);
	
	public void updateRegionBatch(List<Region> list);
	
	public List<Region> findAllRegion();
}
