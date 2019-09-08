package com.orangelala.service.shop.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.order.request.CreateActivityRequest;
import com.orangelala.framework.common.shop.response.code.ActivityCode;
import com.orangelala.framework.common.shop.response.msg.ActivityMsg;
import com.orangelala.framework.common.shop.status.ActivityStatus;
import com.orangelala.framework.model.shop.Activity;
import com.orangelala.framework.model.shop.ActivityDetail;
import com.orangelala.framework.model.shop.DetailRequestModel;
import com.orangelala.service.shop.mapper.ActivityMapper;

/**
 * 促销活动
 * 
 * @author chrilwe
 *
 */
@Service
public class ActivityService {
	
	@Autowired
	private ActivityMapper activityMapper;

	// 商家创建满减促销活动
	@Transactional
	public BaseResponse createActivity(CreateActivityRequest createActivityRequest) throws Exception {
		if(createActivityRequest == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
	
		String endTime = createActivityRequest.getEndTime();
		int grade = createActivityRequest.getGrade();
		int isStartWithOther = createActivityRequest.getIsStartWithOther();
		String shopId = createActivityRequest.getShopId();
		String startTime = createActivityRequest.getStartTime();
		String title = createActivityRequest.getTitle();
		List<DetailRequestModel> detailList = createActivityRequest.getDetailList();
		
		//插入Activity，返回主键自增id
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Activity activity = new Activity();
		activity.setEndTime(sdf.parse(endTime));
		activity.setGrade(grade);
		activity.setIsStartWithOther(isStartWithOther);
		activity.setShopId(shopId);
		activity.setStartTime(sdf.parse(startTime));
		activity.setStatus(ActivityStatus.INIT);
		activity.setTitle(title);
		if(sdf.parse(endTime).before(new Date())) {
			return new BaseResponse(ActivityCode.ACTIVITY_ENDTIME_ERROR,ActivityMsg.ACTIVITY_ENDTIME_ERROR);
		}
		if(sdf.parse(endTime).before(sdf.parse(startTime))) {
			return new BaseResponse(ActivityCode.ACTIVITY_STARTTIME_AFTER_ENDTIME,ActivityMsg.ACTIVITY_STARTTIME_AFTER_ENDTIME);
		}
		activityMapper.insertActivity(activity);
		int id = activity.getId();
		
		//插入ActivityDetail
		for(DetailRequestModel model : detailList) {
			ActivityDetail activityDetail = new ActivityDetail();
			activityDetail.setActivityId(id);
			activityDetail.setPayMoney(model.getPayMoney());
			activityDetail.setPrice(model.getPrice());
			activityDetail.setStatus(ActivityStatus.INIT);
			activityMapper.insertActivityDetail(activityDetail);
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	// 发布促销任务
	@Transactional
	public BaseResponse publishActivity(int activityId) {
		Activity activity = activityMapper.findActivityById(activityId);
		if(activity == null) {
			return new BaseResponse(ActivityCode.ACTIVITY_NOT_EXISTS,ActivityMsg.ACTIVITY_NOT_EXISTS);
		}
		if(activity.getStatus() == ActivityStatus.END) {
			return new BaseResponse(ActivityCode.ACTIVITY_END,ActivityMsg.ACTIVITY_END);
		}
		if(activity.getStatus() == ActivityStatus.NORMAL) {
			return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
		}
		if(activity.getEndTime().before(new Date())) {
			return new BaseResponse(ActivityCode.ACTIVITY_ENDTIME_ERROR,ActivityMsg.ACTIVITY_ENDTIME_ERROR);
		}
		activityMapper.updateActivityStatus(ActivityStatus.NORMAL,activityId);
		activityMapper.updateActivityDetailStatus(ActivityStatus.NORMAL,activityId);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	// 根据商家店铺号查询优先级别较高或者可以叠加的活动
	public List<Activity> findUsefullActivity(String shopId) {
		if(StringUtils.isEmpty(shopId)) {
			return null;
		}
		
		return activityMapper.findUsefullActivity(shopId);
	}
	
	//根据活动号查询activityDetail并且排序
	public Map<Integer, List<ActivityDetail>> findActivityDetailAscByPayMoney(String activityIds) {
		if(StringUtils.isEmpty(activityIds)) {
			return null;
		}
		Map<Integer, List<ActivityDetail>> map = new HashMap<Integer, List<ActivityDetail>>();
		String[] split = activityIds.split(",");
		for(String activityId : split) {
			int aid = Integer.parseInt(activityId);
			List<ActivityDetail> activityDetailList = activityMapper.findActivityDetailAscByPayMoney(aid);
			map.put(aid, activityDetailList);
		}
		return map;
	}
}
