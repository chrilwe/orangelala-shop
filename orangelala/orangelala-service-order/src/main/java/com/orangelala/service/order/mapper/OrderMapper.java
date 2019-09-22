package com.orangelala.service.order.mapper;

import org.apache.ibatis.annotations.Param;

import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.model.order.OrderDetails;

public interface OrderMapper {
	//添加order
	public int addOrder(Order order);
	//删除order
	public int delOrderByOrderNumber(String orderumber);
	//根据id查询order
	public Order findByOrderNumber(String orderNumber);
	//根据id和uid查询
	public Order findByOrderNumberAndUid(@Param("orderNumber")String orderNumber,@Param("userId")String userId);
	//更新订单状态
	public void updateStatusByOrderNumber(@Param("status")String status,@Param("orderNumber")String orderNumber);
}
