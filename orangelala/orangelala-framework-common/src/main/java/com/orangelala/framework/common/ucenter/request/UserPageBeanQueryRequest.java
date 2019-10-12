package com.orangelala.framework.common.ucenter.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class UserPageBeanQueryRequest extends BaseRequest {
	private String nickNameKeyword;//昵称关键字
	private String username;//用户账号
	private int page;
	private int pageSize;
}
