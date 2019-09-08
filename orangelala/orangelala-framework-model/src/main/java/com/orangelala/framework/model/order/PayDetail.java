package com.orangelala.framework.model.order;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 支付详情信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class PayDetail {
	private String id;
	//支付合作伙伴
	private String name;
	//别名
	private String aliase;
	//支付logo
	private String logoPic;
	//状态
	private String status;
	//添加时间
	private Date createTime;
}
