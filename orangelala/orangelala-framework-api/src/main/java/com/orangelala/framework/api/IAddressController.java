package com.orangelala.framework.api;
/**
 * 用户收货地址
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.order.OrderAddress;

public interface IAddressController {
	//添加收货地址
	public BaseResponse addOrderAddress(OrderAddress orderAddress);
	
	//更新收货地址
	public BaseResponse updateOrderAddress(OrderAddress orderAddress);
	
	//删除收货地址
	public BaseResponse deleteOrderAddress(int addressId);
}
