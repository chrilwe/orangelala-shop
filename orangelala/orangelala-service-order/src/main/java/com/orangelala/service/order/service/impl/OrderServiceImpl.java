package com.orangelala.service.order.service.impl;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.order.request.CountOrderPriceRequest;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.order.response.CountOrderPriceResponse;
import com.orangelala.framework.common.order.response.OrderAddResponse;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.model.order.OrderDetails;
import com.orangelala.service.order.mapper.OrderDetailMapper;
import com.orangelala.service.order.mapper.OrderMapper;
import com.orangelala.service.order.service.OrderService;

import io.seata.spring.annotation.GlobalTransactional;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	//提交订单
	@Override
	@GlobalTransactional
	public OrderAddResponse commitOrder(OrderAddRequest orderAddRequest, String userId) {
		//
		return null;
	}

	@Override
	public Order queryOrderById(String orderNumber) {
		if(StringUtils.isEmpty(orderNumber)) {
			return null;
		}
		return orderMapper.findByOrderNumber(orderNumber);
	}

	@Override
	@Transactional
	public BaseResponse cancelOrderById(String orderNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public OrderAddResponse addOrder(OrderAddRequest orderAddRequest, String userId) {
		if(orderAddRequest == null) {
			return null;
		}
		String itemIds = orderAddRequest.getItemIds();
		Order order = orderAddRequest.getOrder();
		String shopId = orderAddRequest.getShopId();
		if(StringUtils.isEmpty(itemIds)) {
			return null;
		}
		
		//TODO 取出预先生成订单
		String orderNumber = "";
		
		List<OrderDetails> list = getItemInfo(itemIds,userId,orderNumber);
		
		return null;
	}

	@Override
	public CountOrderPriceResponse countOrderPrice(CountOrderPriceRequest countRequest, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrderStatus(String status, String orderNumber) {
		
	}

	@Override
	public Order queryOrderByIdAndUid(String orderNumber, String userId) {
		if(StringUtils.isEmpty(orderNumber)) {
			return null;
		}
		
		return orderMapper.findByOrderNumberAndUid(orderNumber, userId);
	}
	
	//从购物车中获取商品信息
	private static final String shopcartKey = "shopcart_";
	private List<OrderDetails> getItemInfo(String itemIds, String userId, String orderNumber) {
		String[] split = itemIds.split(",");
		List<OrderDetails> list = new ArrayList<OrderDetails>();
		for(String itemId : split) {
			String value = (String)stringRedisTemplate.opsForHash().get(shopcartKey+userId, itemId);
			ItemInfo itemInfo = JSON.parseObject(value, ItemInfo.class);
			OrderDetails orderDetail = new OrderDetails();
			orderDetail.setItemId(itemId);
			orderDetail.setItemNum(itemInfo.getNum());
			orderDetail.setItemPrice(Integer.parseInt(itemInfo.getPrice()+""));
			orderDetail.setItemTitle(itemInfo.getTitle());
			orderDetail.setOrderNumber(orderNumber);
			list.add(orderDetail);
		}
		return list;
	}

}
