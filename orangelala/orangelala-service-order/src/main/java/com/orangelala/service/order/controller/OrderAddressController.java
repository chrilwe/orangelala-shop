package com.orangelala.service.order.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IAddressController;
import com.orangelala.framework.common.base.BaseController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.order.OrderAddress;
import com.orangelala.framework.utils.Oauth2Util;
import com.orangelala.service.order.service.OrderAddressService;

@RestController
@RequestMapping("/orderAddress")
public class OrderAddressController extends BaseController implements IAddressController {
	
	@Autowired
	private OrderAddressService orderAddressService;
	
	//添加收货地址
	@Override
	@PostMapping("/add")
	public BaseResponse addOrderAddress(OrderAddress orderAddress) {
		
		return orderAddressService.addOrderAddress(orderAddress, getUserId());
	}
	
	//更新收货地址信息
	@Override
	@PostMapping("/update")
	public BaseResponse updateOrderAddress(OrderAddress orderAddress) {
		
		return orderAddressService.updateOrderAddress(orderAddress, getUserId());
	}

	//删除收货地址
	@Override
	@GetMapping("/delete")
	public BaseResponse deleteOrderAddress(int addressId) {
		
		return orderAddressService.deleteOrderAddress(addressId, getUserId());
	}
	
	//解析获取用户id
	private String getUserId() {
		Map<String, String> map = Oauth2Util.getJwtClaimsFromHeader(request);
		String userId = (String)map.get("userId");
		return userId;
	}
}
