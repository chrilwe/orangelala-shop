package com.orangelala.framework.api;
/**
 * 商家店铺满减促销活动
 * @author chrilwe
 *
 */

import java.util.List;
import java.util.Map;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.order.request.CreateActivityRequest;
import com.orangelala.framework.model.shop.Activity;
import com.orangelala.framework.model.shop.ActivityDetail;

public interface IActivityController {
	//商家创建满减促销活动
	public BaseResponse createActivity(CreateActivityRequest createActivityRequest);
	//发布促销任务
	public BaseResponse publishActivity(int activityId);
	//根据商家店铺号查询优先级别较高或者可以叠加的活动
	public List<Activity> findUsefullActivity(String shopId);
	//根据活动号查询activityDetail并且排序
	public Map<Integer, List<ActivityDetail>> findActivityDetailAscByPayMoney(String activityIds);
}
