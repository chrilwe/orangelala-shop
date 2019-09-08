package com.orangelala.service.order.listener.process.impl;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.model.order.Order;
import com.orangelala.service.order.client.CouponClient;
import com.orangelala.service.order.configs.SpringBeanGetter;
import com.orangelala.service.order.listener.process.MessageProcessor;
/**
 * 分布式事务使用优惠券业务
 * @author chrilwe
 *
 */
public class UseCoupon implements MessageProcessor {

	@Override
	public void process(String message) {
		OrderAddRequest orderAddRequest = JSON.parseObject(message, OrderAddRequest.class);
		Order order = orderAddRequest.getOrder();
		String couponIds = order.getCouponIds();
		if(StringUtils.isEmpty(couponIds)) {
			SpringBeanGetter beanGetter = new SpringBeanGetter();
			CouponClient couponClient = beanGetter.getBean(CouponClient.class);
			BaseResponse response = couponClient.useCoupon(couponIds);
		}
	}

}
