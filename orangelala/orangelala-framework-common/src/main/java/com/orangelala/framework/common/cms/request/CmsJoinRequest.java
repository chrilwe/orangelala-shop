package com.orangelala.framework.common.cms.request;

import com.orangelala.framework.common.base.BaseRequest;

import lombok.Data;

@Data
public class CmsJoinRequest extends BaseRequest {
	private String cmsConfigId;
	private String templateIId;
	private String pageId;
	private String siteId;
}
