package com.orangelala.service.order.service;

import javax.servlet.http.HttpServletRequest;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.order.request.CountOrderPriceRequest;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.order.response.CountOrderPriceResponse;
import com.orangelala.framework.common.order.response.OrderAddResponse;
import com.orangelala.framework.model.order.Order;

public interface OrderService {
	// 提交订单
	public OrderAddResponse commitOrder(OrderAddRequest orderAddRequest, String userId);

	// 根据订单号查询订单
	public Order queryOrderById(String orderNumber);
	
	//根据订单号和用户id查询
	public Order queryOrderByIdAndUid(String orderNumber, String userId);

	// 取消订单
	public BaseResponse cancelOrderById(String orderNumber);
	
	//添加订单
	public BaseResponse addOrder(OrderAddRequest orderAddRequest, String userId);
	
	//计算结算金额
	public CountOrderPriceResponse countOrderPrice(CountOrderPriceRequest countRequest, String userId);
	
	//更新订单状态
	public void updateOrderStatus(String status, String orderNumber);
}
