package com.orangelala.service.order.mapper;

import com.orangelala.framework.model.order.OrderAddress;

public interface OrderAddressMapper {
	public OrderAddress add(OrderAddress orderAddress);
	
	public OrderAddress findById(int id);
	
	public int delById(int id);
	
	public int update(OrderAddress orderAddress);
}
