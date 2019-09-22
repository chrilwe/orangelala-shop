package com.orangelala.framework.common.item.inventory.response;

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.inventory.response.code.InventoryCode;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReduceInventoryResponse extends BaseResponse {

	public ReduceInventoryResponse(int code, String msg, List<String> itemIds) {
		super(code, msg);
		if(code == InventoryCode.ITEM_INVENTORY_NO_ENOUGH) {
			if(itemIds == null) {
				throw new RuntimeException("please ask me no adequate items!");
			} else {
				this.isAdequate = false;
			}
		} else {
			this.isAdequate = true;
		}
	}
	
	private List<String> itemIds;//库存不足的商品
	private boolean isAdequate;//库存是否充足
}
