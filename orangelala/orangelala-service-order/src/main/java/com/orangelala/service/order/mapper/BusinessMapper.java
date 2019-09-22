package com.orangelala.service.order.mapper;
/**
 * 多表操作mapper
 * @author chrilwe
 *
 */

import org.springframework.data.repository.query.Param;

import com.orangelala.framework.model.order.OrderCoupon;
import com.orangelala.framework.model.order.OrderInfos;

public interface BusinessMapper {
	public OrderInfos findOrderInfosByOrderNumberAndUid(@Param("orderNumber")String orderNumber, @Param("userId")String userId);

	public int addOrderCoupon(OrderCoupon orderCoupon);
}
