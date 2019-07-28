package com.orangelala.framework.common.cms.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class AddCmsConfigResponse extends BaseResponse {

	public AddCmsConfigResponse(int code, String msg, String cmsConfigId) {
		super(code, msg);
		this.cmsConfigId = cmsConfigId;
	}
	
	private String cmsConfigId;
}
