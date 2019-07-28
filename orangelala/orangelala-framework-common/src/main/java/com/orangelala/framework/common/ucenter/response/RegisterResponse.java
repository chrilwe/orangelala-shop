package com.orangelala.framework.common.ucenter.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;
/**
 * 注册用户结果
 * @author chrilwe
 *
 */
@Data
@ToString
public class RegisterResponse extends BaseResponse {

	public RegisterResponse(int code, String msg) {
		super(code, msg);
		
	}

}
