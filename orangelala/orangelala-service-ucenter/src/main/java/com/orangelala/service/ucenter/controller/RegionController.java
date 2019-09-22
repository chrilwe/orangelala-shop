package com.orangelala.service.ucenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IRegionController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.ucenter.request.QueryRegionList;
import com.orangelala.framework.model.ucenter.Region;
import com.orangelala.framework.model.ucenter.RegionQueryList;
import com.orangelala.framework.model.ucenter.RegionTreeNode;
import com.orangelala.service.ucenter.service.RegionService;
/**
 * 区域信息
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/region")
public class RegionController implements IRegionController {
	
	@Autowired
	private RegionService regionService;
	
	//根据level查询
	@Override
	@GetMapping("/findByLevel")
	public List<Region> findRegionByLevel(int level) {
		
		return regionService.findRegionByLevel(level);
	}
	
	//根据pid查询
	@Override
	@GetMapping("/findBypid")
	public List<Region> findRegionBypid(int pid) {
		
		return regionService.findRegionBypid(pid);
	}
	
	//根据id查询
	@Override
	@GetMapping("/findById")
	public Region findRegionById(int id) {
		
		return regionService.findRegionById(id);
	}
	
	//区域数据树状结构
	@Override
	@GetMapping("/regionTreeNode")
	public RegionTreeNode regionTreeNode() {
		
		return regionService.regionTreeNode();
	}
	
	//分页查询区域数据
	@Override
	@GetMapping("/queryRegionList")
	public RegionQueryList queryRegionList(QueryRegionList queryRequest) {
		
		return regionService.queryRegionList(queryRequest);
	}
	
	//批量查询
	@Override
	@GetMapping("/queryRegionBatch")
	public List<Region> queryRegionBatch(String ids) {
		
		return regionService.queryRegionBatch(ids);
	}
	
	//批量更新
	@Override
	@PostMapping("/updateRegionBatch")
	public BaseResponse updateRegionBatch(List<Region> list) {
		regionService.updateRegionBatch(list);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//更新
	@Override
	@PostMapping("/updateRegion")
	public BaseResponse updateRegion(Region region) {
		regionService.updateRegion(region);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	@GetMapping("/findAllRegion")
	public List<Region> findAllRegion() {
		
		return regionService.findAllRegion();
	}
	
}
