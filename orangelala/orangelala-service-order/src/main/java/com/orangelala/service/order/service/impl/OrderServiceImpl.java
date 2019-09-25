package com.orangelala.service.order.service.impl;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.inventory.response.ReduceInventoryResponse;
import com.orangelala.framework.common.item.inventory.response.msg.InventoryMsg;
import com.orangelala.framework.common.order.request.CountOrderPriceRequest;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.order.response.CountOrderPriceResponse;
import com.orangelala.framework.common.order.response.OrderAddResponse;
import com.orangelala.framework.common.order.response.msg.OrderMsg;
import com.orangelala.framework.common.order.status.OrderStatus;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.model.order.OrderCoupon;
import com.orangelala.framework.model.order.OrderDetails;
import com.orangelala.framework.utils.GenerateNum;
import com.orangelala.service.order.annotation.CheckPrice;
import com.orangelala.service.order.client.InventoryClient;
import com.orangelala.service.order.mapper.BusinessMapper;
import com.orangelala.service.order.mapper.OrderDetailMapper;
import com.orangelala.service.order.mapper.OrderMapper;
import com.orangelala.service.order.service.OrderService;

import io.seata.spring.annotation.GlobalTransactional;

@Service
public class OrderServiceImpl implements OrderService {
	private static final String ORDERNUMBER_KEY = "ordernumber_";
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private InventoryClient inventoryClient;
	@Autowired
	private BusinessMapper businessMapper;
	
	//提交订单
	@Override
	@CheckPrice //校验商品价格和支付总额
	@GlobalTransactional  //seata分布式事务
	@Transactional
	public OrderAddResponse commitOrder(OrderAddRequest orderAddRequest, String userId) {
		//创建订单
		OrderAddResponse addOrder = this.addOrder(orderAddRequest, userId);
		//减少库存
		Map<String, Integer> map = new HashMap<String, Integer>();
		ReduceInventoryResponse reduce = inventoryClient.reduce(map);
		int code = reduce.getCode();
		String msg = reduce.getMsg();
		if(code != Code.SUCCESS) {
			if(msg.equals(InventoryMsg.ITEM_INVENTORY_NO_ENOUGH)) {
				throw new RuntimeException(msg);
			} else {
				throw new RuntimeException(Msg.SYSTEM_ERROR);
			}
		}
		return addOrder;
	}

	@Override
	public Order queryOrderById(String orderNumber) {
		if(StringUtils.isEmpty(orderNumber)) {
			return null;
		}
		return orderMapper.findByOrderNumber(orderNumber);
	}

	@Override
	public OrderAddResponse addOrder(OrderAddRequest orderAddRequest, String userId) {
		if(orderAddRequest == null) {
			return null;
		}
		String itemIds = orderAddRequest.getItemIds();
		Order order = orderAddRequest.getOrder();
		String shopId = orderAddRequest.getShopId();
		String couponIds = orderAddRequest.getCouponIds();
		if(StringUtils.isEmpty(itemIds)) {
			return null;
		}
		
		//取出预先生成订单
		String orderNumber = "";
		boolean flag = true;
		try {
			orderNumber = stringRedisTemplate.opsForList().leftPop(ORDERNUMBER_KEY);
		} catch (Exception e) {
			flag = false;
		}
		if(StringUtils.isEmpty(orderNumber) || !flag) {
			GenerateNum n = new GenerateNum();
			orderNumber = n.generate();
		}
		List<ItemInfo> list = getItemInfo(itemIds,userId);
		List<OrderDetails> list1 = new ArrayList<OrderDetails>();
		for(ItemInfo itemInfo : list) {
			OrderDetails orderDetail = new OrderDetails();
			orderDetail.setItemId(itemInfo.getId());
			orderDetail.setItemNum(itemInfo.getNum());
			orderDetail.setItemPrice(Integer.parseInt(itemInfo.getPrice()+""));
			orderDetail.setItemTitle(itemInfo.getTitle());
			orderDetail.setOrderNumber(orderNumber);
			list1.add(orderDetail);
		}
		
		//创建订单
		order.setCreateTime(new Date());
		order.setOrderNumber(orderNumber);
		order.setStatus(OrderStatus.PAY_NO);
		order.setUserId(userId);
		orderMapper.addOrder(order);
		orderDetailMapper.addBatch(list1);
		if(!StringUtils.isEmpty(couponIds)) {
			String[] split = couponIds.split(",");
			for(String string : split) {
				int couponId = Integer.parseInt(string);
				OrderCoupon orderCoupon = new OrderCoupon();
				orderCoupon.setCouponId(couponId);
				orderCoupon.setOrderNumber(orderNumber);
				businessMapper.addOrderCoupon(orderCoupon);
			}
		}
		return new OrderAddResponse(Code.SUCCESS,Msg.SUCCESS,orderNumber,0);
	}

	@Override
	@Transactional
	public void updateOrderStatus(String status, String orderNumber, String userId) {
		if(StringUtils.isEmpty(orderNumber)) {
			throw new RuntimeException(OrderMsg.PARAM_NULL);
		}
		orderMapper.updateStatusByOrderNumberAndUid(status, orderNumber,userId);
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
	public List<ItemInfo> getItemInfo(String itemIds, String userId) {
		String[] split = itemIds.split(",");
		List<ItemInfo> list = new ArrayList<ItemInfo>();
		for(String itemId : split) {
			String value = (String)stringRedisTemplate.opsForHash().get(shopcartKey+userId, itemId);
			ItemInfo itemInfo = JSON.parseObject(value, ItemInfo.class);
			list.add(itemInfo);
		}
		return list;
	}

	@Override
	@Transactional
	public void updateOrderStatus(String status, String orderNumber) {
		orderMapper.updateStatusByOrderNumber(status, orderNumber);
	}

}
