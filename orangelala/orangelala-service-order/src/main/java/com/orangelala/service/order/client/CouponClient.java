package com.orangelala.service.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.coupon.Coupon;

@FeignClient(ServiceList.orangelala_service_price)
@RequestMapping("/price")
public interface CouponClient {
	
	@GetMapping("/findByCouponId")
	public Coupon findByCouponId(int couponId);
	
	@GetMapping("/useCoupon")
	public BaseResponse useCoupon(String couponIds);
	
}
