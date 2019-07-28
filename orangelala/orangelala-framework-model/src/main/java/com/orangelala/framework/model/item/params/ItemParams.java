package com.orangelala.framework.model.item.params;

import lombok.Data;
import lombok.ToString;

/**
 * 商品规格
 * @author chrilwe
 *
 */
@Data
@ToString
public class ItemParams {
	private String id;
	private String itemId;
	private String type;//产品类型
	private String ingredlient;//配料
	private String gb;//产品标准号
	private String method;//使用方法
	private String itemHome;//产品产地
	private String weight;//产品规格
	private String qs;//产品生产许可证
	private int qgp;//保质期/天
	private String storage;//储存方法
}
