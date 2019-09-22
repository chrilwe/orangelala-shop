package com.orangelala.service.ucenter;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.ucenter.request.QueryRegionList;
import com.orangelala.framework.model.ucenter.Region;
import com.orangelala.framework.model.ucenter.RegionQueryList;
import com.orangelala.service.ucenter.service.RegionService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestService {
	
	@Autowired
	private RegionService regionService;
	
	@Test
	public void testQueryRegionBatch() {
		List<Region> list = regionService.queryRegionBatch("110101,"+"110102");
		System.out.println(JSON.toJSONString(list));
	}
	
	@Test
	public void testQueryRegionList() {
		QueryRegionList queryRequest = new QueryRegionList();
		queryRequest.setKeyword("东");
		queryRequest.setLevel(1);
		queryRequest.setPage(1);
		queryRequest.setSize(10);
		RegionQueryList list = regionService.queryRegionList(queryRequest);
		System.out.println(JSON.toJSONString(list));
	}
	
}
