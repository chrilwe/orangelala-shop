package com.orangelala.framework.common.ucenter.request;

import com.orangelala.framework.common.base.BaseRequest;
import com.orangelala.framework.model.ucenter.User;

import lombok.Data;
/**
 * 注册用户 
 * @author chrilwe
 *
 */
@Data
public class RegisterRequest extends BaseRequest {
	private User user;
}
