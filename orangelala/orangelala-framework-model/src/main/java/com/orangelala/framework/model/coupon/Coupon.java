package com.orangelala.framework.model.coupon;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 优惠券
 * @author chrilwe
 *
 */
@Data
@ToString
public class Coupon {
	private int id;//自增主键
	private String name;
	private String type;//优惠券类型
	private String status;
	private int money;//优惠金额
	private int salesMoney;//满减金额
	private Date startTime;
	private Date endTime;
	private Date createTime;
	private int getTimes;//领取次数限制
	private int couponNum;//生成优惠券的数量 
	private String shopId;//优惠券所属商铺
	private int isUsedWithOther;//是否叠加 0不叠加，1叠加
	private int version;
}
