package com.orangelala.service.auth.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.auth.response.code.SmsCode;
import com.orangelala.framework.common.auth.response.msg.SmsMsg;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.model.auth.SmsCodeInfo;
import com.orangelala.service.auth.service.SmsService;
import com.orangelala.service.auth.util.SmsUtil;

@Service
public class SmsServiceImpl implements SmsService {
	private static final String CODE_KEY = "code_";
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Value("${sms.resendTimes}")
	private int smsResendTimes;
	@Value("${sms.accountSid}")
	private String accountSid;
	@Value("${sms.authToken}")
	private String authToken;
	@Value("${sms.templateId}")
	private String templateId;
	@Value("${sms.baseUrl}")
	private String baseUrl;
	@Value("${sms.codeTimeout}")
	private int codeTimeout;
	
	//发送短信验证码
	@Override
	public BaseResponse sendSmsVerificationCode(String phoneNumber) {
		long currentTimeMillis = System.currentTimeMillis();
		if(StringUtils.isEmpty(phoneNumber)) {
			return new BaseResponse(SmsCode.PHOME_NUMBER_NULL,SmsMsg.PHONE_NUMBER_NULL);
		}
		
		//查询当前时间是否在上次发送时间之后60秒
		SmsCodeInfo smsCodeInfo = this.getSmsCodeInfo(phoneNumber);
		if(smsCodeInfo != null) {
			Date sendTime = smsCodeInfo.getSendTime();
			if((currentTimeMillis - sendTime.getTime())/100 > smsResendTimes) {
				return new BaseResponse(SmsCode.PLEASE_SEND_AFTER_60_SEC,SmsMsg.PLEASE_SEND_AFTER_60_SEC);
			}
		}
		
		//生成6位随机数 
		String code = "";
		for(int i = 0;i < 6;i++) {
			Random random = new Random();
			int num = random.nextInt(9);
			code += num;
		}
		
		//将6位随机数发送到手机
		StringBuilder sb = new StringBuilder();
		sb.append("accountSid").append("=").append(accountSid);
		sb.append("&to").append("=").append(phoneNumber);
		try {
			sb.append("&param").append("=").append(URLEncoder.encode(code,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sb.append("&templateid").append("=").append(templateId);
		String body = sb.toString() + SmsUtil.createCommonParam(accountSid, authToken);
		String result = SmsUtil.post(baseUrl, body);
		
		//将验证码存入redis，5分钟后过期
		SmsCodeInfo codeInfo = new SmsCodeInfo();
		codeInfo.setCode(code);
		codeInfo.setPhoneNumber(phoneNumber);
		codeInfo.setSendTime(new Date(currentTimeMillis));
		stringRedisTemplate.boundValueOps(CODE_KEY+phoneNumber).set(JSON.toJSONString(codeInfo), codeTimeout, TimeUnit.MINUTES);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}

	@Override
	public SmsCodeInfo getSmsCodeInfo(String phoneNumber) {
		if(StringUtils.isEmpty(phoneNumber)) {
			return null;
		}
		String value = stringRedisTemplate.boundValueOps(CODE_KEY+phoneNumber).get();
		if(StringUtils.isEmpty(value)) {
			return null;
		}
		SmsCodeInfo smsCodeInfo = JSON.parseObject(value, SmsCodeInfo.class);
		return smsCodeInfo;
	}

}
