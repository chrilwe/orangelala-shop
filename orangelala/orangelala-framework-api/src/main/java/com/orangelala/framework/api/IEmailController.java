package com.orangelala.framework.api;
/**
 * 邮箱服务
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.auth.EmailCodeInfo;

public interface IEmailController {
	//发送邮件
	public BaseResponse sendEmail(String email);
	//获取邮件验证码
	public EmailCodeInfo getEmailCode(String email);
}
