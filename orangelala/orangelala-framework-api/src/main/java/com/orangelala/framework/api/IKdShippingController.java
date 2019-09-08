package com.orangelala.framework.api;
/**
 * 快递鸟服务
 * @author chrilwe
 *
 */

import java.util.Map;

import com.orangelala.framework.common.base.BaseResponse;

public interface IKdShippingController {
	//快递鸟在线下单
	public BaseResponse createOrderOnlineByJson(Map params);
	//快递鸟取消下单
	public BaseResponse cancelOrderOnlineByJson(Map params);
}
