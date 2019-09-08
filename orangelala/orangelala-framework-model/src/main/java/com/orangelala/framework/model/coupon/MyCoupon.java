package com.orangelala.framework.model.coupon;

import com.orangelala.framework.model.coupon.Coupon;

import lombok.Data;
import lombok.ToString;

/**
 * 我的优惠券
 * @author chrilwe
 *
 */
@Data
@ToString
public class MyCoupon extends Coupon {
	private boolean timeout;//是否过期
	private int owner;//优惠券数量
}
