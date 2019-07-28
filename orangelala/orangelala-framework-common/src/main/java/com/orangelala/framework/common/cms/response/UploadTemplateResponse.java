package com.orangelala.framework.common.cms.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UploadTemplateResponse extends BaseResponse {

	public UploadTemplateResponse(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}
	
	private String templateFileId;
}
