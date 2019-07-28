package com.orangelala.framework.common.cms.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class QueryCmsTemplateListRequest extends BaseRequest {
	private String templateName;
	
}
