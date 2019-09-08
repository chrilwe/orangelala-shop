package com.orangelala.service.order.mapper;

import org.apache.ibatis.annotations.Param;

import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.model.order.OrderDetails;

public interface OrderMapper {
	//添加order
	public int addOrder(Order order);
	//删除order
	public int delOrderById(String orderId);
	//根据id查询order
	public Order findOrderById(String orderId);
	//添加orderDetail
	public int addOrderDetail(OrderDetails orderDetails);
	//删除orderDetails
	public int delOrderDetailById(String orderId);
	//更新订单状态
	public void updateOrderStatus(@Param("status")String status,@Param("orderNumber")String orderNumber);
	//更新订单商品详情状态
	public void updateOrderDetailsStatus(@Param("status")String status,@Param("orderNumber")String orderNumber);
}
