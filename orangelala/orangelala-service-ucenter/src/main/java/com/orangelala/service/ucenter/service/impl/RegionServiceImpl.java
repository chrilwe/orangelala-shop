package com.orangelala.service.ucenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.orangelala.framework.common.ucenter.request.QueryRegionList;
import com.orangelala.framework.model.ucenter.Region;
import com.orangelala.framework.model.ucenter.RegionQueryList;
import com.orangelala.framework.model.ucenter.RegionTreeNode;
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
	public Region findRegionById(int id) {
		
		return regionMapper.findRegionById(id);
	}
	
	
	//树状结构数据
	@Override
	public RegionTreeNode regionTreeNode() {
		
		return regionMapper.queryRegionTreeNode();
	}
	
	//条件分页查询区域数据
	@Override
	public RegionQueryList queryRegionList(QueryRegionList queryRequest) {
		if(queryRequest == null) {
			return null;
		}
		int level = queryRequest.getLevel();
		int size = queryRequest.getSize();
		int page = queryRequest.getPage();
		String keyword = queryRequest.getKeyword();
		RegionQueryList list = new RegionQueryList();
		//默认查询数据条数
		if(size <= 0) {
			size = 10;
		}
		//默认当前页为1
		if(page <= 0) {
			page = 1;
		}
		//统计总条数
		
		//过滤条件区域等级
		if(level > 0) {
			//关键字搜索条件存在
			if(StringUtils.isEmpty(keyword)) {
				return regionMapper.queryRegionListBylevelAndKeyword(level, "%"+keyword+"%", (page-1)*size, size);
				
			}
			//关键字搜索条件不存在
			return regionMapper.queryRegionListByLevel(level, (page-1)*size, size);
		} else {
		//没有过滤条件
			if(StringUtils.isEmpty(keyword)) {
				return regionMapper.queryRegionListByKeyword("%"+keyword+"%", (page-1)*size, size);
			}
			
		}
		return regionMapper.queryRegionList((page-1)*size, size);
	}

	@Override
	public List<Region> queryRegionBatch(String ids) {
		if(StringUtils.isEmpty(ids)) {
			return null;
		}
		String[] split = ids.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for(String string : split) {
			int id = Integer.parseInt(string);
			list.add(id);
		}
		return regionMapper.queryRegionBatcByIdList(list);
	}

	@Override
	@Transactional
	public void updateRegionBatch(List<Region> list) {
		if(list == null) {
			return;
		}
		regionMapper.updateRegionBatch(list);
	}

	@Override
	@Transactional
	public void updateRegion(Region region) {
		regionMapper.updateRegion(region);
	}

	@Override
	public List<Region> findAllRegion() {
		
		return regionMapper.findAllRegion();
	}

}
