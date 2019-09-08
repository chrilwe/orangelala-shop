package com.orangelala.service.coupon.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.orangelala.framework.api.ICouponController;
import com.orangelala.framework.common.auth.response.msg.AuthMsg;
import com.orangelala.framework.common.base.BaseController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.coupon.exception.CouponException;
import com.orangelala.framework.common.coupon.request.CreateCouponRequest;
import com.orangelala.framework.model.coupon.Coupon;
import com.orangelala.framework.model.coupon.MyCoupon;
import com.orangelala.framework.utils.Oauth2Util;
import com.orangelala.service.coupon.service.CouponService;
/**
 * 优惠券服务
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/coupon")
public class CouponController extends BaseController implements ICouponController {
	
	@Autowired
	private CouponService couponService;
	
	//查询当前可用优惠券
	@Override
	@GetMapping("/findUsefullCoupon")
	public List<Coupon> findAllUsefullCoupon(int totalPay, String shopId) {
		String userId = getUserId(request);
		List<Coupon> list = couponService.findAllUsefullCoupon(totalPay, shopId, userId);
		return list;
	}
	
	//领取优惠券
	@Override
	@GetMapping("/getCoupon")
	public BaseResponse getCoupon(int couponId) {
		String userId = getUserId(request);
		BaseResponse response = couponService.getCoupon(couponId, userId);
		return response;
	}
	
	//使用优惠券
	@Override
	@GetMapping("/useCoupon")
	public BaseResponse useCoupon(String couponIds) {
		String userId = getUserId(request);
		BaseResponse response = couponService.useCoupon(couponIds, userId);
		return response;
	}
	
	//商家创建优惠券活动
	@Override
	@PostMapping("/createCoupon")
	public BaseResponse createCoupon(CreateCouponRequest createCouponRequest) {
		String userId = getUserId(request);
		if(createCouponRequest == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		String formatString = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		BaseResponse response = null;
		try {
			Coupon coupon = new Coupon();
			coupon.setCouponNum(createCouponRequest.getCouponNum());
			coupon.setEndTime(sdf.parse(createCouponRequest.getEndTime()));
			coupon.setGetTimes(createCouponRequest.getGetTimes());
			coupon.setIsUsedWithOther(createCouponRequest.getIsUsedWithOther());
			coupon.setMoney(createCouponRequest.getMoney());
			coupon.setName(createCouponRequest.getName());
			coupon.setShopId(createCouponRequest.getShopId());
			coupon.setSalesMoney(createCouponRequest.getSalesMoney());
			coupon.setStartTime(sdf.parse(createCouponRequest.getStartTime()));
			coupon.setType(createCouponRequest.getType());
			response = couponService.createCoupon(coupon, userId);
		} catch (ParseException e) {
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return response;
	}

	@Override
	@GetMapping("/cancelCoupon")
	public BaseResponse cancelCoupon(int couponId, String shopId) {
		String userId = getUserId(request);
		BaseResponse response = couponService.cancelCoupon(couponId, shopId, userId);
		return response;
	}
	
	
	//获取userId
	private String getUserId(HttpServletRequest request) {
		Map<String, String> map = Oauth2Util.getJwtClaimsFromHeader(request);
		if(map != null) {
			String userId = map.get("userId");
			if(!StringUtils.isEmpty(userId)) {
				return userId;
			} else {
				throw new CouponException(AuthMsg.UNAUTHORIZED);
			}
		}
		return null;
	}

	@Override
	@GetMapping("/mycoupns")
	public List<MyCoupon> myCoupons() {
		String userId = getUserId(request);
		List<MyCoupon> myCoupons = couponService.myCoupons(userId);
		return myCoupons;
	}

	@Override
	@GetMapping("/findByCouponId")
	public Coupon findByCouponId(int couponId) {
		
		return couponService.findByCouponId(couponId);
	}

	@Override
	@GetMapping("/queryBatch")
	public List<Coupon> queryBatch(String couponIds) {
		
		return couponService.queryBatch(couponIds);
	}
}
