package com.orangelala.framework.model.item.inventory;

import java.util.Date;

import lombok.Data;
import lombok.ToString;
/**
 * 更新库存记录
 * @author chrilwe
 *
 */
@Data
@ToString
public class ItemInventoryRecord {
	private long id;//主键自增
	private String itemId;//商品id
	private String orderNumber;//订单号
	private String userId;//用户id
	private int num;//库存更新数量
	private Date updateTime;//更新库存时间
}
