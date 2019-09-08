package com.orangelala.service.order.task;

import com.orangelala.framework.common.order.status.OrderStatus;
import com.orangelala.framework.model.order.Order;
import com.orangelala.service.order.configs.SpringBeanGetter;
import com.orangelala.service.order.service.OrderService;

/**
 * 监控订单是否过期线程
 * @author chrilwe
 *
 */
public class OrderPayMonitorTask implements Runnable {

	@Override
	public void run() {
		try {
			Thread.sleep(timeout);
			SpringBeanGetter bean = new SpringBeanGetter();
			OrderService orderService = bean.getBean(OrderService.class);
			Order order = orderService.queryOrderById(orderNumber);
			if(order.getStatus().equals(OrderStatus.PAY_NO)) {
				orderService.updateOrderStatus(OrderStatus.PAY_CANCEL, orderNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public OrderPayMonitorTask(long timeout,String orderNumber) {
		this.orderNumber = orderNumber;
		this.timeout = timeout;
	}
	
	private String orderNumber;
	private long timeout;
}
