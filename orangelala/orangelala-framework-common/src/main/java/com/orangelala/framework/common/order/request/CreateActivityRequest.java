package com.orangelala.framework.common.order.request;

import java.util.Date;
import java.util.List;

import com.orangelala.framework.common.base.BaseRequest;
import com.orangelala.framework.model.shop.DetailRequestModel;

import lombok.Data;

@Data
public class CreateActivityRequest extends BaseRequest {
	private String shopId;
	private String title;//活动标题
	private String startTime;//活动开始时间
	private String endTime;//活动结束时间
	private int grade;//活动优先级
	private int isStartWithOther;//是否与其他活动叠加
	private List<DetailRequestModel> detailList;
}
