package com.orangelala.framework.model.seckill;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 秒杀订单信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class SeckillOrderMessage {
	private String orderID;
	private String itemId;
	private Date createTime;
	private String userID;
}
