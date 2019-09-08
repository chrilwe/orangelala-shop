package com.orangelala.service.ucenter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IRegionController;
import com.orangelala.framework.model.ucenter.Region;
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

	@Override
	@GetMapping("/findByLevel")
	public List<Region> findRegionByLevel(int level) {
		
		return regionService.findRegionByLevel(level);
	}

	@Override
	@GetMapping("/findBypid")
	public List<Region> findRegionBypid(int pid) {
		
		return regionService.findRegionBypid(pid);
	}

	@Override
	@GetMapping("/findBylevellikename")
	public List<Region> findRegionByLevelAndLikeName(int level, String keyword) {
		
		return regionService.findRegionByLevelAndLikeName(level, keyword);
	}

	@Override
	@GetMapping("/findById")
	public Region findRegionById(int id) {
		
		return regionService.findRegionById(id);
	}

}
