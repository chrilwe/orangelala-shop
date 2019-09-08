package com.orangelala.service.item.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IItemController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.request.QueryItemListRequest;
import com.orangelala.framework.common.item.response.QueryItemListResponse;
import com.orangelala.framework.model.item.Item;
import com.orangelala.framework.model.item.ItemInfo;
import com.orangelala.framework.model.item.category.ItemCategory;
import com.orangelala.framework.model.item.details.ItemDetails;
import com.orangelala.framework.model.item.params.ItemParams;
@RestController
@RequestMapping("/item")
public class ItemController implements IItemController {
	
	//添加商品信息
	@Override
	@PostMapping("/info/add")
	public BaseResponse addItem(Item item) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//添加商品分类
	@Override
	@PostMapping("/category/add")
	public BaseResponse addItemCategory(ItemCategory itemCategory) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//添加商品参数
	@Override
	@PostMapping("/params/add")
	public BaseResponse addItemParams(ItemParams itemParams) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//添加商品详情
	@Override
	@PostMapping("/details/add")
	public BaseResponse addItemDetails(ItemDetails itemDetails) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//页面预览
	@Override
	@GetMapping("/preview")
	public BaseResponse previewItemPage(String pageId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//页面发布
	@Override
	@GetMapping("/publish")
	public BaseResponse publishItem(String itemId, String pageId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	//商品列表
	@Override
	@GetMapping("/list")
	public QueryItemListResponse queryItems(QueryItemListRequest queryRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	//添加商品库存
	@Override
	@PostMapping("/inventory/add")
	public BaseResponse addItemInventory(String itemId, int num) {
		// TODO Auto-generated method stub
		return null;
	}

	//添加商品价格
	@Override
	@PostMapping("/price/add")
	public BaseResponse addItemPrice(String itemId, long price) {
		// TODO Auto-generated method stub
		return null;
	}

	//根据id查询
	@Override
	@GetMapping("/iteminfo/find")
	public ItemInfo findItemInfoById(String itemId) {
		// TODO Auto-generated method stub
		return null;
	}

}
