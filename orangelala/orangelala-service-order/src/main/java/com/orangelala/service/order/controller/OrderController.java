package com.orangelala.service.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IOrderController;
import com.orangelala.framework.common.base.BaseController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.order.response.OrderAddResponse;
import com.orangelala.framework.model.order.Order;
import com.orangelala.service.order.service.OrderService;
import com.orangelala.service.order.service.PayService;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController implements IOrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private PayService payService;
	
	//提交订单
	@Override
	@PostMapping("/add")
	public OrderAddResponse commitOrder(OrderAddRequest orderAddRequest) {
		OrderAddResponse commitOrder = orderService.commitOrder(orderAddRequest, request);
		return commitOrder;
	}
	
	//根据订单号查询订单
	@Override
	@GetMapping("/queryById")
	public Order queryOrderById(String orderNumber) {
		Order order = orderService.queryOrderById(orderNumber);
		return order;
	}
	
	//取消订单
	@Override
	@GetMapping("/cancel")
	public BaseResponse cancelOrderById(String orderNumber) {
		BaseResponse cancelOrderById = orderService.cancelOrderById(orderNumber);
		return cancelOrderById;
	}

	@Override
	@GetMapping("/shippingFee/query")
	public int queryShippingFee(String shippingId) {
		
		return 0;
	}

}
