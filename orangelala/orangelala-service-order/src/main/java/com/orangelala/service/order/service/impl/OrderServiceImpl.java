package com.orangelala.service.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.item.inventory.request.UpdateItemNumRequest;
import com.orangelala.framework.common.item.price.request.QueryItemPriceRequest;
import com.orangelala.framework.common.item.price.response.QueryItemPriceResponse;
import com.orangelala.framework.common.order.request.CountOrderPriceRequest;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.order.response.CountOrderPriceResponse;
import com.orangelala.framework.common.order.response.OrderAddResponse;
import com.orangelala.framework.common.order.response.code.OrderCode;
import com.orangelala.framework.common.order.response.msg.OrderMsg;
import com.orangelala.framework.common.order.status.OrderStatus;
import com.orangelala.framework.model.coupon.Coupon;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.item.price.ItemPrice;
import com.orangelala.framework.model.message.MessageModel;
import com.orangelala.framework.model.message.type.MessageDataType;
import com.orangelala.framework.model.order.Order;
import com.orangelala.framework.model.order.OrderDetails;
import com.orangelala.framework.model.order.status.Status;
import com.orangelala.framework.model.shop.Activity;
import com.orangelala.framework.model.shop.ActivityDetail;
import com.orangelala.framework.model.ucenter.User;
import com.orangelala.framework.utils.GenerateNum;
import com.orangelala.framework.utils.Oauth2Util;
import com.orangelala.service.order.client.ActivityClient;
import com.orangelala.service.order.client.CouponClient;
import com.orangelala.service.order.client.InventoryClient;
import com.orangelala.service.order.client.MessageClient;
import com.orangelala.service.order.client.PriceClient;
import com.orangelala.service.order.mapper.OrderMapper;
import com.orangelala.service.order.service.OrderService;
import com.orangelala.service.order.task.OrderPayMonitorTask;

@Service
public class OrderServiceImpl implements OrderService {
	private static final ExecutorService pool = Executors.newSingleThreadExecutor();
	private static final String orderNumberKey = "OrderNumber_";
	private static final String shopcartKey = "shopcart_";
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private InventoryClient inventoryClient;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private MessageClient messageClient;
	@Autowired
	private PriceClient priceClient;
	@Autowired
	private ActivityClient activityClient;
	@Autowired
	private CouponClient couponClient;
	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;
	@Value("${order.expire}")
	private int expire;

	@Override
	@Transactional
	public OrderAddResponse commitOrder(OrderAddRequest orderAddRequest, HttpServletRequest request) {
		if(orderAddRequest == null) {
			return new OrderAddResponse(Code.PARAM_NULL,Msg.PARAM_NULL,"",0);
		}
		String itemIds = orderAddRequest.getItemIds();
		Order order = orderAddRequest.getOrder();
		if(StringUtils.isEmpty(itemIds) || order == null) {
			return new OrderAddResponse(Code.PARAM_NULL,Msg.PARAM_NULL,"",0);
		}
		User userInfo = this.getUserInfo(request);
		String userId = userInfo.getId();
		
		//校验订单总金额是否正确
		CountOrderPriceRequest countRequest = new CountOrderPriceRequest();
		countRequest.setItemIds(itemIds);
		countRequest.setOrder(order);
		CountOrderPriceResponse countOrderPrice = this.countOrderPrice(countRequest, request);
		int price = order.getPrice();
		int initialPrice = order.getInitialPrice();
		int c_price = countOrderPrice.getPrice();
		int c_initPrice = countOrderPrice.getInitPrice();
		if(price != c_price || c_initPrice != initialPrice) {
			return new OrderAddResponse(OrderCode.ORDER_PRICE_ERROR,OrderMsg.ORDER_PRICE_ERROR,"",0);
		}
		
		//从redis中取出预先生成的订单号,订单号不够用需要即刻生成,并通知管理员增加订单号的数量
		String orderNumber = stringRedisTemplate.opsForList().leftPop(orderNumberKey);
		if(StringUtils.isEmpty(orderNumber)) {
			GenerateNum num = new GenerateNum();
			orderNumber = num.generate();
		}
		
		//更新库存
		List<UpdateItemNumRequest> updateRequestList = new ArrayList<UpdateItemNumRequest>();
		String[] split = itemIds.split(",");
		for(String itemId : split) {
			String value = (String)stringRedisTemplate.opsForHash().get(shopcartKey+userId, itemId);
			if(!StringUtils.isEmpty(value)) {
				ItemInfo itemInfo = JSON.parseObject(value, ItemInfo.class);
				int num = itemInfo.getNum();
				UpdateItemNumRequest updateRequest = new UpdateItemNumRequest();
				updateRequest.setItemId(itemId);
				updateRequest.setBuyItemNum(num);
				updateRequest.setOrderNumber(orderNumber);
				updateRequest.setUserId(userId);
				updateRequestList.add(updateRequest);
			}
		}
		String messageId = UUID.randomUUID().toString();
		MessageModel messageModel = new MessageModel();
		messageModel.setMessageId(messageId);
		messageModel.setDataType(MessageDataType.JSON);
		order.setUserId(userId);
		order.setOrderNumber(orderNumber);
		orderAddRequest.setOrder(order);
		messageModel.setMessageBody(JSON.toJSONString(orderAddRequest));
		messageClient.readySendMessage(messageModel);
		
		BaseResponse updateBatch = inventoryClient.updateBatch(updateRequestList);
		if(updateBatch.getCode() != Code.SUCCESS) {
			return new OrderAddResponse(updateBatch.getCode(),updateBatch.getMsg(),"",0);
		}
		//创建订单,优惠券更新为已使用(异步创建)
		messageClient.sendingMessage(messageId);
		return new OrderAddResponse(Code.SUCCESS,Msg.SUCCESS,orderNumber,order.getPrice());
	}

	@Override
	public Order queryOrderById(String orderNumber) {
		if(StringUtils.isEmpty(orderNumber)) {
			return null;
		}
		Order order = orderMapper.findOrderById(orderNumber);
		return order;
	}

	@Override
	@Transactional
	public BaseResponse cancelOrderById(String orderNumber) {
		if(StringUtils.isEmpty(orderNumber)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		//查询该订单
		Order order = orderMapper.findOrderById(orderNumber);
		if(order == null) {
			return new BaseResponse(OrderCode.ORDER_NOT_EXISTS,OrderMsg.ORDER_NOT_EXISTS);
		}
		String status = order.getStatus();
		if(!Status.NOPAY.equals(status)) {
			return new BaseResponse(OrderCode.STATUS_ERROR,OrderMsg.STATUS_ERROR);
		}
		//删除订单
		orderMapper.delOrderById(orderNumber);
		orderMapper.delOrderDetailById(orderNumber);
		return new BaseResponse(OrderCode.SUCCESS,OrderMsg.SUCCESS);
	}
	
	private User getUserInfo(HttpServletRequest request) {
		Map<String, String> userInfo = Oauth2Util.getJwtClaimsFromHeader(request);
		if(userInfo == null) {
			return null;
		}
		String userId = userInfo.get("userId");
		String username = userInfo.get("username");
		String userPic = userInfo.get("userPic");
		String utype = userInfo.get("utype");
		if(StringUtils.isEmpty(userId)) {
			return null;
		}
		User user = new User();
		user.setUsername(username);
		user.setId(userId);
		user.setUserpic(userPic);
		user.setUtype(utype);
		return user;
	}

	@Override
	@Transactional
	public BaseResponse addOrder(OrderAddRequest orderAddRequest, HttpServletRequest request) {
		if(orderAddRequest == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		User userInfo = this.getUserInfo(request);
		String userId = userInfo.getId();
		String itemIds = orderAddRequest.getItemIds();
		Order order = orderAddRequest.getOrder();
		
		order.setStartTime(new Date());
		order.setStatus(OrderStatus.PAY_NO);
		orderMapper.addOrder(order);
		
		if(!StringUtils.isEmpty(itemIds)) {
			String[] split = itemIds.split(",");
			for(String itemId : split) {
				String value = (String) stringRedisTemplate.opsForHash().get(shopcartKey+userId, itemId);
				ItemInfo itemInfo = JSON.parseObject(value, ItemInfo.class);
				OrderDetails orderDetails = new OrderDetails();
				orderDetails.setItemId(itemId);
				orderDetails.setItemNum(itemInfo.getNum());
				orderDetails.setItemPrice(Integer.parseInt(itemInfo.getPrice()+""));
				orderDetails.setItemTitle(itemInfo.getTitle());
				orderDetails.setOrderNumber(order.getOrderNumber());
				orderDetails.setStartTime(new Date());
				orderMapper.addOrderDetail(orderDetails);
			}
			
			//清空购物车(异步)
			pool.submit(new Runnable() {

				@Override
				public void run() {
					for(String itemId : split) {
						stringRedisTemplate.opsForHash().delete(shopcartKey+userId, itemId);
					}
				}
				
			});
			
			//监控订单支付是否超时
			threadPoolExecutor.submit(new OrderPayMonitorTask(expire,order.getOrderNumber()));
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	
	//计算结算金额
	@Override
	public CountOrderPriceResponse countOrderPrice(CountOrderPriceRequest countRequest, HttpServletRequest request) {
		if(countRequest == null) {
			return new CountOrderPriceResponse(Code.PARAM_NULL,Msg.PARAM_NULL,0,0);
		}
		User userInfo = this.getUserInfo(request);
		String userId = userInfo.getId();
		String itemIds = countRequest.getItemIds();
		Order order = countRequest.getOrder();
		String couponIds = countRequest.getCouponIds();
		String shopId = order.getShopId();
		int activityCount = 0;//优惠金额
		//实际商品费用
		String[] split = itemIds.split(",");
		int totalPrice = 0;
		for(String itemId : split) {
			String value = (String) stringRedisTemplate.opsForHash().get(shopcartKey+userId, itemId);
			if(StringUtils.isEmpty(value)) {
				return new CountOrderPriceResponse(OrderCode.SHOPCART_NOT_INCLUDE_ITEM,OrderMsg.SHOPCART_NOT_INCLUDE_ITEM,0,0);
			}
			ItemInfo itemInfo = JSON.parseObject(value, ItemInfo.class);
			long price = itemInfo.getPrice();
			//检验价格是否发生变动
			QueryItemPriceRequest request1 = new QueryItemPriceRequest();
			request1.setItemId(itemId);
			QueryItemPriceResponse response = priceClient.queryItemPrice(request1);
			long price2 = response.getPrice();
			if(price != price2) {
				return new CountOrderPriceResponse(OrderCode.PRICE_CHANGEED,OrderMsg.PRICE_CHANGED,0,0);
			}
			totalPrice += price;
		}
		int initPrice = totalPrice;
		
		//满减优惠
		List<ActivityDetail> l = new ArrayList<ActivityDetail>();
		List<Activity> activityList = activityClient.findUsefullActivity(shopId);
		if(activityList != null) {
			String activityIds = "";
			for(Activity activity : activityList) {
				int id = activity.getId();
				activityIds += id;
			}
			Map<Integer, List<ActivityDetail>> map = activityClient.findActivityDetailAscByPayMoney(activityIds);
			if(map != null) {
				for(Iterator iterator = map.keySet().iterator();iterator.hasNext();) {
					String key = (String) iterator.next();
					List<ActivityDetail> activityDetailList = map.get(key);
					int index = 0;
					for(int i = 0;i < activityDetailList.size();i++) {
						int payMoney = activityDetailList.get(i).getPayMoney();//满减
						int price = activityDetailList.get(i).getPrice();//可以优惠金额
						if(price > totalPrice) {
							index = i - 1;
							break;
						}
					}
					if(index >= 0) {
						int p = activityDetailList.get(index).getPrice();
						totalPrice = totalPrice - p;
					}
				}
			}
		}
		
		//TODO 邮费
		
		//优惠券
		if(!StringUtils.isEmpty(couponIds)) {
			String[] split2 = couponIds.split(",");
			for(String couponId : split2) {
				int cid = Integer.parseInt(couponId);
				Coupon coupon = couponClient.findByCouponId(cid);
				int money = coupon.getMoney();
				totalPrice = totalPrice - money;
			}
		}

		return new CountOrderPriceResponse(Code.SUCCESS,Msg.SUCCESS,initPrice,totalPrice);
	}

	@Override
	@Transactional
	public void updateOrderStatus(String status, String orderNumber) {
		orderMapper.updateOrderStatus(status, orderNumber);
		orderMapper.updateOrderDetailsStatus(status, orderNumber);
	}

}
