package com.orangelala.framework.model.order;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 物流信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class ShippingDetail {
	private String id;
	//物流名称
	private String name;
	//物流别名
	private String aliase;
	//物流logo
	private String logoPic;
	//添加时间
	private Date createTime;
	//状态
	private String shippingStatus;
}
