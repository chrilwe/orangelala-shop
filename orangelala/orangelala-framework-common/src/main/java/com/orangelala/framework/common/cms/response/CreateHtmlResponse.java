package com.orangelala.framework.common.cms.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class CreateHtmlResponse extends BaseResponse {

	public CreateHtmlResponse(int code, String msg, String pageId, String html) {
		super(code, msg);
		this.pageId = pageId;
		this.html = html;
	}
	
	private String pageId;
	private String html;
}
