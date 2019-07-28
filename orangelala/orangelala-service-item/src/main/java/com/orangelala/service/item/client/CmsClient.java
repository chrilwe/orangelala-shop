package com.orangelala.service.item.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.cms.CmsPage;
import com.orangelala.framework.model.cms.CmsTemplate;

/**
 * 页面管理客户端
 * @author chrilwe
 *
 */
@FeignClient(ServiceList.orangelala_service_cms)
@RequestMapping("/cms")
public interface CmsClient {
	
	@GetMapping("/cms_page/find")
	public CmsPage queryCmsPageById(String pageId);
	
	@GetMapping("/cms_template/find")
	public CmsTemplate queryCmsTemplateById(String templateId);
}
