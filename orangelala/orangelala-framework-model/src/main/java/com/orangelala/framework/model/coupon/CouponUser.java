package com.orangelala.framework.model.coupon;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CouponUser {
	private int id;//自增主键
	private int couponId;
	private String userId;
	private int status;//是否已使用:0未使用，1已使用
	private Date createTime;
	private Date updateTime;
}
