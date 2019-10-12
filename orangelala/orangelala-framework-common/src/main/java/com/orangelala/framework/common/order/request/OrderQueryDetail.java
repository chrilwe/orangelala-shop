package com.orangelala.framework.common.order.request;

import lombok.Data;

/**
 * 订单条件查询
 * @author chrilwe
 *
 */
@Data
public abstract class OrderQueryDetail {
	
	private String orderNumber;
	private String payType;
	private int currentPage;
	private int pageSize;
	private String orderStatus;

}
