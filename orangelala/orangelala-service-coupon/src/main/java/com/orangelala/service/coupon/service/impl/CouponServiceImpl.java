package com.orangelala.service.coupon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.coupon.exception.CouponException;
import com.orangelala.framework.common.coupon.response.code.CouponCode;
import com.orangelala.framework.common.coupon.response.msg.CouponMsg;
import com.orangelala.framework.common.coupon.status.CouponStatus;
import com.orangelala.framework.model.coupon.Coupon;
import com.orangelala.framework.model.coupon.CouponUser;
import com.orangelala.framework.model.coupon.MyCoupon;
import com.orangelala.service.coupon.mapper.CouponMapper;
import com.orangelala.service.coupon.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService {
	
	@Autowired
	private CouponMapper couponMapper;
	
	//用户查询所有可用的优惠券
	@Override
	public List<Coupon> findAllUsefullCoupon(int totalPay, String shopId, String userId) {
		//查询当前用户未过期未使用的店铺优惠券和全品类优惠券
		//参数2:0.未使用,1.已使用
		List<Coupon> coupons = couponMapper.findUsefullCouponList(userId, 0, shopId);
		return coupons;
	}
	
	
	//用户领取优惠券
	@Override
	public BaseResponse getCoupon(int couponId, String userId) {
		//查询当前id的优惠券的状态为正常用户领取的次数小于最大领取次数的优惠券
		Coupon coupon = couponMapper.findCouponById(couponId);
		if(coupon == null) {
			return new BaseResponse(CouponCode.COUPON_NOT_EXISTS,CouponMsg.COUPON_NOT_EXISTS);
		}
		Date endTime = coupon.getEndTime();
		int couponNum = coupon.getCouponNum();
		int getTimes = coupon.getGetTimes();
		int version = coupon.getVersion();
		String status = coupon.getStatus();
		Date currentTime = new Date();
		if(status.equals(CouponStatus.CANCEL)) {
			return new BaseResponse(CouponCode.COUPON_ACTIVITY_END,CouponMsg.COUPON_ACTIVITY_END);
		}
		if(endTime.before(currentTime)) {
			return new BaseResponse(CouponCode.COUPON_TIME_OUT,CouponMsg.COUPON_TIME_OUT);
		}
		if(couponNum <= 0) {
			return new BaseResponse(CouponCode.COUPON_ACTIVITY_END,CouponMsg.COUPON_ACTIVITY_END);
		}
		int ownerCouponNum = couponMapper.count_couponId(couponId,userId);
		if(getTimes <= ownerCouponNum) {
			return new BaseResponse(CouponCode.COUPON_GET_TIMES_0,CouponMsg.COUPON_GET_TIMES_0);
		}
		
		//用户关联优惠券
		CouponUser couponUser = new CouponUser();
		couponUser.setCouponId(couponId);
		couponUser.setCreateTime(new Date());
		couponUser.setStatus(0);//0未使用，1已使用
		couponUser.setUserId(userId);
		couponMapper.addCouponUser(couponUser);
		
		//更新优惠券数量
		couponMapper.updateCouponNum(version, couponId);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//使用优惠券
	@Override
	@Transactional
	public BaseResponse useCoupon(String couponIds, String userId) {
		if(StringUtils.isEmpty(couponIds)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		Date currentTime = new Date();
		String[] split = couponIds.split(",");
		List<Coupon> list = new ArrayList<Coupon>();
		List<List<CouponUser>> list1 = new ArrayList<List<CouponUser>>();
		for (String String : split) {
			int couponId = Integer.parseInt(String);
			//查询用户是否有当前未使用优惠券
			List<CouponUser> couponUsers = couponMapper.findCouponUserByCouponIdAndUserIdAndStatus(couponId, userId, 0);
			if(couponUsers == null) {
				throw new CouponException(CouponMsg.COUPON_NOT_EXISTS);
			}
			list1.add(couponUsers);
			
			//查询优惠券是否过期
			Coupon coupon = couponMapper.findCouponById(couponId);
			Date endTime = coupon.getEndTime();
			if(endTime.before(currentTime)) {
				throw new CouponException(CouponMsg.COUPON_NOT_EXISTS);
			}
			list.add(coupon);
			
		}
		
		//至少需要split.length -1 数量的优惠券可以叠加
		int flag = 0;
		for(Coupon coupon: list) {
			int isUsedWithOther = coupon.getIsUsedWithOther();//0:不能叠加，1：可以叠加
			if(isUsedWithOther == 1) {
				flag += 1;
			}
		}
		if(flag < (split.length - 1)) {
			throw new CouponException(CouponMsg.COUPON_NOT_ALLOWED_USED_WITH_);
		}
		
		//将优惠券状态改为已使用
		for(int i = 0;i < split.length;i++) {
			//取出一张优惠券使用
			CouponUser couponUser = list1.get(i).get(0);
			couponUser.setStatus(1);
			couponUser.setUpdateTime(currentTime);
			couponUser.setUserId(userId);
			couponMapper.updateCouponUserStatus(couponUser);
		}
		
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//商家创建优惠券活动
	@Override
	@Transactional
	public BaseResponse createCoupon(Coupon coupon, String userId) {
		if(coupon == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		int couponNum = coupon.getCouponNum();
		if(couponNum <= 0) {
			return new BaseResponse(CouponCode.COUPON_NUM_IS_0,CouponMsg.COUPON_NUM_IS_0);
		}
		//没有设置抽取优惠券次数默认抽取次数为1
		int getTimes = coupon.getGetTimes();
		if(getTimes <= 0) {
			getTimes = 1;
			coupon.setGetTimes(getTimes);
		}
		//开始时间和结束时间不能在当前时间之前
		Date startTime = coupon.getStartTime();
		Date endTime = coupon.getEndTime();
		Date currentTime = new Date();
		if(startTime.before(currentTime) || endTime.before(currentTime) || endTime.before(startTime)) {
			return new BaseResponse(CouponCode.STARTTIME_OR_ENDTIME_ERROR,CouponMsg.STARTTIME_OR_ENDTIME_ERROR);
		}
		
		coupon.setCreateTime(currentTime);
		coupon.setStatus(CouponStatus.NORMAL);
		coupon.setVersion(1);
		couponMapper.addCoupon(coupon);
		return new BaseResponse(CouponCode.SUCCESS, CouponMsg.SUCCESS);
	}
	
	
	//商家取消优惠券活动(不包括已发出的优惠券)
	@Override
	@Transactional
	public BaseResponse cancelCoupon(int couponId, String shopId, String userId) {
		if(StringUtils.isEmpty(shopId) || couponId < 0) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		
		//查询当前优惠券活动是否已经结束
		Coupon coupon = couponMapper.findCouponByCouponIdAndShopId(couponId, shopId, CouponStatus.NORMAL);
		if(coupon == null) {
			return new BaseResponse(CouponCode.COUPON_CANCEL_OR_END,CouponMsg.COUPON_CANCEL_OR_END);
		}
		
		coupon.setStatus(CouponStatus.CANCEL);
		couponMapper.updateCouponStatus(coupon);
		return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
	}

	//我的优惠券（列出未使用的优惠券）
	@Override
	public List<MyCoupon> myCoupons(String userId) {
		Date currentTime = new Date();
		List<MyCoupon> coupons = couponMapper.findNoUsedCoupon(userId, 0);//0未使用
		if(coupons != null) {
			for (MyCoupon myCoupon : coupons) {
				Date endTime = myCoupon.getEndTime();
				if(endTime.before(currentTime)) {
					myCoupon.setTimeout(true);
				} else {
					myCoupon.setTimeout(false);
				}
			}
		}
		return coupons;
	}


	@Override
	public Coupon findByCouponId(int couponId) {
		Coupon coupon = couponMapper.findCouponById(couponId);
		return coupon;
	}


	@Override
	public List<Coupon> queryBatch(String couponIds) {
		String[] split = couponIds.split(",");
		List<Coupon> couponList = new ArrayList<Coupon>();
		for (String couponId : split) {
			Coupon coupon = couponMapper.findCouponById(Integer.parseInt(couponIds));
			couponList.add(coupon);
		}
		return couponList;
	}

}
