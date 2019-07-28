package com.orangelala.framework.model.shop;

import java.util.Date;

import lombok.Data;
import lombok.ToString;
/**
 * 商铺实体
 * @author chrilwe
 *
 */
@Data
@ToString
public class Shop {
	//商铺号
	private String id;
	//商铺名称
	private String shopName;
	//商铺拥有者
	private String userId;
	//商铺描述
	private String desc;
	//商铺状态
	private String status;
	//商铺创建日期
	private Date createTime;
}
