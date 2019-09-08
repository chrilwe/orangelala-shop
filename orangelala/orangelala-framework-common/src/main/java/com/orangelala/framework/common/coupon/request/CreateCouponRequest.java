package com.orangelala.framework.common.coupon.request;

import java.util.Date;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class CreateCouponRequest extends BaseRequest {
	private String name;
	private String type;//优惠券类型
	private int money;//优惠金额
	private int salesMoney;//满减金额
	private String startTime;
	private String endTime;
	private int getTimes;//领取次数限制
	private int couponNum;//生成优惠券的数量 
	private String shopId;//优惠券所属商铺
	private int isUsedWithOther;
}
