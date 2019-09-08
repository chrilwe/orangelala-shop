package com.orangelala.service.coupon.service;

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.coupon.Coupon;
import com.orangelala.framework.model.coupon.MyCoupon;

public interface CouponService {
	// 列出当前用户可用的所有优惠券
	public List<Coupon> findAllUsefullCoupon(int totalPay, String shopId, String userId);

	// 用户领取优惠券
	public BaseResponse getCoupon(int couponId, String userId);

	// 用户使用优惠券
	public BaseResponse useCoupon(String couponIds, String userId);

	// 商家创建优惠券活动
	public BaseResponse createCoupon(Coupon coupon, String userId);

	// 商家取消优惠券活动
	public BaseResponse cancelCoupon(int couponId, String shopId, String userId);
	
	//我的优惠券
	public List<MyCoupon> myCoupons(String userId);
	
	//根据优惠券id查询未使用未过期的优惠券
	public Coupon findByCouponId(int couponId);
	
	//批量查询优惠券接口
	public List<Coupon> queryBatch(String couponIds);
}
