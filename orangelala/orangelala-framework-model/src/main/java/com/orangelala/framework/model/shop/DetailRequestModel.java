package com.orangelala.framework.model.shop;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DetailRequestModel {
	private int payMoney;//满减金额
	private int price;//减少金额
}
