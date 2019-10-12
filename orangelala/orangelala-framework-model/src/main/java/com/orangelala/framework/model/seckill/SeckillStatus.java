package com.orangelala.framework.model.seckill;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 秒杀状态
 * @author chrilwe
 *
 */
@Data
@ToString
public class SeckillStatus {
	private String itemID;
	private String userID;
	private String status;//秒杀状态: 排队中,抢购成功，抢购失败，未支付，已支付，支付中
	private Date createTime;
}
