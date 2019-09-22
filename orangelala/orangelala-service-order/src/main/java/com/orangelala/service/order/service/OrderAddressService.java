package com.orangelala.service.order.service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.order.OrderAddress;

public interface OrderAddressService {
	//添加收货地址
	public BaseResponse addOrderAddress(OrderAddress orderAddress, String userId);
		
	//更新收货地址
	public BaseResponse updateOrderAddress(OrderAddress orderAddress, String userId);
		
	//删除收货地址
	public BaseResponse deleteOrderAddress(int addressId, String userId);
}
