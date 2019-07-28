package com.orangelala.service.shop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.IShopController;
import com.orangelala.framework.common.shop.request.ApplayOpenShopRequest;
import com.orangelala.framework.common.shop.request.QueryShopsRequest;
import com.orangelala.framework.common.shop.response.AggreeOpenShopResponse;
import com.orangelala.framework.common.shop.response.ApplayOpenShopResponse;
import com.orangelala.framework.model.shop.Shop;

@RestController
@RequestMapping("/shop")
public class ShopController implements IShopController {

	@Override
	@GetMapping("/applay_open")
	public ApplayOpenShopResponse applayOpenShop(ApplayOpenShopRequest OpenShopRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@GetMapping("/aggree_open")
	public AggreeOpenShopResponse aggreeOpenShop(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@GetMapping("/query")
	public Shop queryShopInfo(String shopId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@GetMapping("/list")
	public List<Shop> queryShops(QueryShopsRequest queryShopsRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
