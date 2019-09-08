package com.orangelala.framework.model.shop;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * 店铺促销活动模型
 * @author chrilwe
 *
 */
@Data
@ToString
public class Activity {
	private int id;//主键自增
	private String shopId;
	private String title;//活动标题
	private Date startTime;//活动开始时间
	private Date endTime;//活动结束时间
	private int grade;//活动优先级
	private int isStartWithOther;//是否与其他活动叠加
	private int status;//活动状态
}
