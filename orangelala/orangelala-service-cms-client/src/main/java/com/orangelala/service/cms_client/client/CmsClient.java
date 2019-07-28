package com.orangelala.service.cms_client.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.cms.CmsPage;

/**
 * 页面管理
 * @author chrilwe
 *
 */
@FeignClient(ServiceList.orangelala_service_cms)
@RequestMapping("/cms")
public interface CmsClient {
	
	@GetMapping("/cms_page/find")
	public CmsPage queryCmsPageById(String pageId);
	
	
}
