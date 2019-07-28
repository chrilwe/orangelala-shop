package com.orangelala.service.price.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IPriceController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.item.price.request.QueryItemPriceRequest;
import com.orangelala.framework.common.item.price.request.UpdateItemPriceRequest;
import com.orangelala.framework.common.item.price.response.QueryItemPriceResponse;
import com.orangelala.framework.model.item.price.ItemPrice;
import com.orangelala.service.price.service.ItemPriceService;
/**
 * 商品价格管理
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/price")
public class PriceController implements IPriceController {
	
	@Autowired
	private ItemPriceService itemPriceService;
	
	//查询商品价格
	@Override
	@GetMapping("/query")
	public QueryItemPriceResponse queryItemPrice(QueryItemPriceRequest request) {
		
		return itemPriceService.queryItemPrice(request);
	}
	
	//更新商品价格
	@Override
	@PostMapping("/update")
	public BaseResponse updateItemPrice(UpdateItemPriceRequest request) {
		
		return itemPriceService.updateItemPrice(request);
	}
	
	//添加商品价格
	@Override
	@PostMapping("/add")
	public BaseResponse addItemPrice(ItemPrice itemPrice) {
		
		return itemPriceService.addItemPrice(itemPrice);
	}

}
