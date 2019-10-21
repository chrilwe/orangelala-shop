package com.orangelala.framework.model.seckill;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 秒杀活动商品详情
 * @author chrilwe
 *
 */
@Data
@ToString
public class SeckillItemInfo {
	private String itemID;
	private String itemName;
	private int initPrice;//初始价格
	private int seckillPrice;//秒杀价格
	private String images;//商品图片
	private String isStart;//秒杀是否开始: yes or no
	private Date startTime;//秒杀开始时间
	private Date endTime;//秒杀结束时间
	private String status;//激活,未激活
	private int inventory;//秒杀商品库存
	private int payTimeLimitSec;//支付超时时间（秒）
}
