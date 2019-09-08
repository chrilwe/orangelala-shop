package com.orangelala.framework.api;
/**
 * 商品管理
 * @author chrilwe
 *
 */

import java.util.List;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.request.QueryItemListRequest;
import com.orangelala.framework.common.item.response.QueryItemListResponse;
import com.orangelala.framework.model.item.Item;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.item.category.ItemCategory;
import com.orangelala.framework.model.item.details.ItemDetails;
import com.orangelala.framework.model.item.inventory.ItemInventory;
import com.orangelala.framework.model.item.params.ItemParams;
import com.orangelala.framework.model.item.price.ItemPrice;

public interface IItemController {
	//新增商品(商户管理)
	public BaseResponse addItem(Item item);
	//添加商品分类(商户管理)
	public BaseResponse addItemCategory(ItemCategory itemCategory);
	//添加商品参数(商户管理)
	public BaseResponse addItemParams(ItemParams itemParams);
	//添加商品详细信息(商户管理)
	public BaseResponse addItemDetails(ItemDetails itemDetails);
	//商品详情页预览(商户管理)
	public BaseResponse previewItemPage(String pageId);
	//商品发布(商户管理)
	public BaseResponse publishItem(String itemId,String pageId);
	//商品列表(商户管理)
	public QueryItemListResponse queryItems(QueryItemListRequest queryRequest);
	//添加商品库存(商户管理)
	public BaseResponse addItemInventory(String itemId,int num);
	//添加商品价格(商户管理)
	public BaseResponse addItemPrice(String itemId,long price);
	//根据商品id查询商品
	public ItemInfo findItemInfoById(String itemId);
}
