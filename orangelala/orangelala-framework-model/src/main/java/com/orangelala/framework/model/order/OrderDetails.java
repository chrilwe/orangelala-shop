package com.orangelala.framework.model.order;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 订单每一种商品详情
 * @author chrilwe
 *
 */
@Data
@ToString
public class OrderDetails {
	
	private String id;
    private String orderNumber;
    private String itemId;
    private Integer itemNum;
    private int itemPrice;
    private String valid;
    private Date startTime;
    private Date endTime;
	private String itemTitle;
	//邮费
	private int shippingFee;
}
