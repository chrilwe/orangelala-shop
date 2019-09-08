package com.orangelala.service.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IActivityController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.order.request.CreateActivityRequest;
import com.orangelala.framework.model.shop.Activity;
import com.orangelala.framework.model.shop.ActivityDetail;
import com.orangelala.service.shop.service.ActivityService;
/**
 * 商家店铺促销活动
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/activity")
public class ActivityController implements IActivityController {
	
	@Autowired
	private ActivityService activityService;
	
	//创建促销活动
	@Override
	@PostMapping("/create")
	public BaseResponse createActivity(CreateActivityRequest createActivityRequest) {
		BaseResponse createActivity = null;
		try {
			createActivity = activityService.createActivity(createActivityRequest);
		} catch (Exception e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return createActivity;
	}
	
	//发布促销活动
	@Override
	@GetMapping("/publish")
	public BaseResponse publishActivity(int activityId) {
		
		return activityService.publishActivity(activityId);
	}
	
	//查询优先级别最高并且可以叠加的活动
	@Override
	@GetMapping("/findUsefullActivity")
	public List<Activity> findUsefullActivity(String shopId) {
		
		return activityService.findUsefullActivity(shopId);
	}
	
	//根据activityId查询并排序
	@Override
	@GetMapping("/findActivityDetailByAid")
	public Map<Integer, List<ActivityDetail>> findActivityDetailAscByPayMoney(String activityIds) {
		
		return activityService.findActivityDetailAscByPayMoney(activityIds);
	}

}
