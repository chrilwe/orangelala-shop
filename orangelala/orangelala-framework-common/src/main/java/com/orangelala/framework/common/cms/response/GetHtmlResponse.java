package com.orangelala.framework.common.cms.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class GetHtmlResponse extends BaseResponse {

	public GetHtmlResponse(int code, String msg, String html) {
		super(code, msg);
		this.html = html;
	}
	private String html;
}
