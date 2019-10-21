package com.orangelala.service.seckill.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orangelala.framework.api.ISeckillController;
import com.orangelala.framework.common.base.BaseController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.model.seckill.SeckillDetail;
import com.orangelala.framework.model.seckill.SeckillStatus;
import com.orangelala.service.seckill.execption.SeckillCode;
import com.orangelala.service.seckill.execption.msg.SeckillExceptionMsg;
import com.orangelala.service.seckill.service.SeckillService;

@RestController
@RequestMapping("/seckill")
public class SeckillController extends BaseController implements ISeckillController {
	
	@Autowired
	private SeckillService seckillService; 
	
	//秒杀入口
	@GetMapping("/start/{itemId}")
	public BaseResponse seckill(@PathVariable("itemId")String itemId) {
		if(StringUtils.isEmpty(itemId)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		
		BaseResponse seckill = null;
		try {
			seckill = seckillService.seckill(itemId, userId);
		} catch (Exception e) {
			if(e.getMessage().equals(SeckillExceptionMsg.SECKILL_ITEMINFO_NO_EXISTS)) {
				return new BaseResponse(SeckillCode.SECKILL_ITEMINFO_NO_EXISTS,SeckillExceptionMsg.SECKILL_ITEMINFO_NO_EXISTS);
			} else if(e.getMessage().equals(SeckillExceptionMsg.SECKILL_END)) {
				return new BaseResponse(SeckillCode.SECKILL_ACTIVITY_OR_END,SeckillExceptionMsg.SECKILL_END);
			} else if(e.getMessage().equals(SeckillExceptionMsg.SECKILL_WAITING_BEGIN)) {
				return new BaseResponse(SeckillCode.SECKILL_WAITING_BEGIN,SeckillExceptionMsg.SECKILL_WAITING_BEGIN);
			} else {
				return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
			}
		}
		return seckill;
	}
	
	
	//查询当前用户秒杀结果
	@GetMapping("/querystatus/{itemId}")
	public SeckillStatus seckillStatus(@PathVariable("itemId")String itemId) {
		
		return seckillService.queryKillResult(itemId, userId);
	}
	
	
	//查询秒杀活动商品库存数量
	@GetMapping("/queryinventory/{itemId}")
	public int queryInventory(@PathVariable("itemId")String itemId) {
		if(StringUtils.isEmpty(itemId)) {
			return 0;
		}
		return seckillService.querySeckillItemInventory(itemId);
	}
	
	//激活秒杀活动
	@GetMapping("/startUp/{itemId}")
	public BaseResponse startUp(@PathVariable("itemId")String itemId) {
		return seckillService.startUpSeckillActivity(itemId);
	}
}
