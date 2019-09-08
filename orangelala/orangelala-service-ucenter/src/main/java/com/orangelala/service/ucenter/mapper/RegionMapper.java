package com.orangelala.service.ucenter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.orangelala.framework.model.ucenter.Region;
import com.orangelala.service.ucenter.mapper.sql.RegionSqlString;

public interface RegionMapper {
	@Select(RegionSqlString.findRegionByLevel)
	public List<Region> findRegionByLevel(int level);
	
	@Select(RegionSqlString.findRegionBypid)
	public List<Region> findRegionBypid(int pid);
	
	@Select(RegionSqlString.findRegionByLevelAndLikeName)
	public List<Region> findRegionByLevelAndLikeName(@Param("level")int level,@Param("name")String name);

	@Select(RegionSqlString.findRegionById)
	public Region findRegionById(int id);
}
