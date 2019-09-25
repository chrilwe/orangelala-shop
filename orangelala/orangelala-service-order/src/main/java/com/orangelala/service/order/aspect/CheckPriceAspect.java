package com.orangelala.service.order.aspect;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.orangelala.framework.common.item.price.response.msg.ItemPriceMsg;
import com.orangelala.framework.common.order.request.OrderAddRequest;
import com.orangelala.framework.common.order.response.msg.OrderMsg;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.item.price.ItemPrice;
import com.orangelala.service.order.client.PriceClient;
import com.orangelala.service.order.service.OrderService;

@Component
@Aspect
public class CheckPriceAspect implements Ordered {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private PriceClient priceClient;

	@Override
	public int getOrder() {
		return 1000;
	}
	
	@Pointcut("execution(* com.orangelala.service.order.aspect.CheckPriceAspect.*(com.orangelala.framework.common.order.request.OrderAddRequest,String)) && @annotation(com.orangelala.service.ucenter.annotation.TestAnnotation) && args(countRequest,userId)")
	public void pointCut(OrderAddRequest countRequest, String userId) {}
	
	@Around("pointCut(countRequest,userId)")
	public int around(ProceedingJoinPoint joinPoint, OrderAddRequest countRequest, String userId) throws Throwable {
		//检查价格是否发生变动
		String itemIds = countRequest.getItemIds();
		int commitTotalPrice = countRequest.getOrder().getPrice();
		List<ItemInfo> itemInfoList = orderService.getItemInfo(itemIds, userId);
		List<ItemPrice> itemPriceList = priceClient.queryPriceBatch(itemIds);
		long totalPrice = 0l;
		for(ItemInfo itemInfo : itemInfoList) {
			String id = itemInfo.getId();
			for(ItemPrice itemPrice : itemPriceList) {
				String itemId = itemPrice.getItemId();
				if(id.equals(itemId)) {
					long price = itemInfo.getPrice();
					long price2 = itemPrice.getPrice();
					if(price != price2) {
						throw new RuntimeException(ItemPriceMsg.ITEM_PRICE_CHANGE + "&&" + itemId);
					}
					break;
				}
			}
			totalPrice += itemInfo.getPrice();
		}
		//检查订单金额是否正确
		boolean checkTotalPay = checkTotalPay(totalPrice, Long.parseLong(commitTotalPrice+""));
		if(!checkTotalPay) {
			throw new RuntimeException(OrderMsg.ORDER_PRICE_ERROR);
		}
		//执行业务
		int result = (int) joinPoint.proceed();
		return result;
	}
	
	//TODO
	private boolean checkTotalPay(long totalPrice, long commitTotalPrice) {
		
		return true;
	}
}
