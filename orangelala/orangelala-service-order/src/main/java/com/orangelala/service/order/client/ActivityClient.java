package com.orangelala.service.order.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.model.shop.Activity;
import com.orangelala.framework.model.shop.ActivityDetail;

/**
 * 满减活动
 * @author chrilwe
 *
 */
@FeignClient
@RequestMapping("/activity")
public interface ActivityClient {
	
	//查询优先级别最高并且可以叠加的活动
	@GetMapping("/findUsefullActivity")
	public List<Activity> findUsefullActivity(String shopId);
	
	@GetMapping("/findActivityDetailByAid")
	public Map<Integer, List<ActivityDetail>> findActivityDetailAscByPayMoney(String activityId);
}
