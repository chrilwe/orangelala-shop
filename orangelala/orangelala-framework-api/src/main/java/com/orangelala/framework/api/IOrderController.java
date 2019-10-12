package com.orangelala.framework.api;
/**
 * 订单管理
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.PageBean;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.order.request.OrderQueryDetail;
import com.orangelala.framework.common.order.response.OrderAddResponse;
import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.model.order.OrderInfos;

public interface IOrderController {
	//提交订单订单
	public OrderAddResponse commitOrder(OrderAddRequest orderAddRequest);
	
	//根据订单号查询订单
	public Order queryOrderById(String orderNumber);
	
	public OrderInfos queryOrderInfosById(String orderNumber);
	
	//取消订单
	public BaseResponse cancelOrderById(String orderNumber);
	
	//查询当前邮费
	public int queryShippingFee(String shippingId);
	
	//条件分页
	public PageBean orderPageBean(OrderQueryDetail orderQueryDetail);
}
