package com.orangelala.framework.model.order;

import lombok.Data;
import lombok.ToString;

/**
 * 收货地址
 * @author chrilwe
 *
 */
@Data
@ToString
public class OrderAddress {
	private int id;
	//收货人
	private String receiver;
	//省
	private String province;
	//市
	private String city;
	//区
	private String district;
	//详细住址
	private String addressDetail;
	private String userId;
}
