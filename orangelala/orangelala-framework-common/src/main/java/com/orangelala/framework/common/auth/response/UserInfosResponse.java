package com.orangelala.framework.common.auth.response;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.model.auth.UserInfos;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserInfosResponse extends BaseResponse {
	private UserInfos userInfos;
	public UserInfosResponse(int code, String msg, UserInfos userInfos) {
		super(code, msg);
		this.userInfos = userInfos;
	}

}
