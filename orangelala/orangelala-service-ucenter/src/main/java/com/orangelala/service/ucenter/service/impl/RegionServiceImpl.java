package com.orangelala.service.ucenter.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.orangelala.framework.model.ucenter.Region;
import com.orangelala.service.ucenter.mapper.RegionMapper;
import com.orangelala.service.ucenter.service.RegionService;
/**
 * 区域信息
 * @author chrilwe
 *
 */
@Service
public class RegionServiceImpl implements RegionService {
	
	@Autowired
	private RegionMapper regionMapper;
	
	@Override
	public List<Region> findRegionByLevel(int level) {
		
		return regionMapper.findRegionByLevel(level);
	}

	@Override
	public List<Region> findRegionBypid(int pid) {
		
		return regionMapper.findRegionBypid(pid);
	}

	@Override
	public List<Region> findRegionByLevelAndLikeName(int level, String keyword) {
		if(StringUtils.isEmpty(keyword)) {
			return null;
		}
		return regionMapper.findRegionByLevelAndLikeName(level, "%"+keyword+"%");
	}

	@Override
	public Region findRegionById(int id) {
		
		return regionMapper.findRegionById(id);
	}

}
