package com.orangelala.framework.model.seckill;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 秒杀请求详情
 * @author chrilwe
 *
 */
@Data
@ToString
public class SeckillDetail {
	private String itemID;//抢购商品ID
	private String userID;//抢购用户ID
	private Date requestTime;//抢购时间
}
