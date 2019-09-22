package com.orangelala.service.order.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.model.order.OrderAddress;
import com.orangelala.service.order.mapper.OrderAddressMapper;
import com.orangelala.service.order.service.OrderAddressService;

@Service
public class OrderAddressServiceImpl implements OrderAddressService {
	
	@Autowired
	private OrderAddressMapper orderAddressMapper;

	@Override
	@Transactional
	public BaseResponse addOrderAddress(OrderAddress orderAddress, String userId) {
		if(orderAddress == null) {
			return new BaseResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		}
		orderAddress.setUserId(userId);
		OrderAddress add = orderAddressMapper.add(orderAddress);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	@Transactional
	public BaseResponse updateOrderAddress(OrderAddress orderAddress, String userId) {
		if(orderAddress == null) {
			return new BaseResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		}
		orderAddress.setUserId(userId);
		orderAddressMapper.update(orderAddress);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	@Transactional
	public BaseResponse deleteOrderAddress(int addressId, String userId) {
		orderAddressMapper.delById(addressId);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

}
