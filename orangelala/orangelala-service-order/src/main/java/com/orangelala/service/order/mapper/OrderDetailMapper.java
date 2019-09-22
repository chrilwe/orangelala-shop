package com.orangelala.service.order.mapper;

import java.util.List;
import com.orangelala.framework.model.order.OrderDetails;

public interface OrderDetailMapper {
	public OrderDetails add(OrderDetails orderDetails);
	
	public int deleteByOrderNumber(String orderNumber);
	
	public List<OrderDetails> findByOrderNumber(String orderNumber);
	
	public int addBatch(List<OrderDetails> list);
}
