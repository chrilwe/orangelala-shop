package com.orangelala.service.order.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IOrderController;
import com.orangelala.framework.common.base.BaseController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.inventory.response.code.InventoryCode;
import com.orangelala.framework.common.item.inventory.response.msg.InventoryMsg;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.order.response.OrderAddResponse;
import com.orangelala.framework.common.order.status.OrderStatus;
import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.utils.Oauth2Util;
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
	@PostMapping("/commit")
	public OrderAddResponse commitOrder(OrderAddRequest orderAddRequest) {
		OrderAddResponse commitOrder = null;
		try {
			commitOrder = orderService.commitOrder(orderAddRequest, getUserId());
		} catch (Exception e) {
			String message = e.getMessage();
			if(message.equals(InventoryMsg.ITEM_INVENTORY_NO_ENOUGH)) {
				return new OrderAddResponse(InventoryCode.ITEM_INVENTORY_NO_ENOUGH,InventoryMsg.ITEM_INVENTORY_NO_ENOUGH,null,0);
			} else {
				return new OrderAddResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR,null,0);
			}
		}
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
		orderService.updateOrderStatus(OrderStatus.PAY_CANCEL, orderNumber, getUserId());
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	@GetMapping("/shippingFee/query")
	public int queryShippingFee(String shippingId) {
		
		return 0;
	}
	
	//解析获取用户id
	private String getUserId() {
		Map<String, String> map = Oauth2Util.getJwtClaimsFromHeader(request);
		String userId = (String)map.get("userId");
		return userId;
	}
}
