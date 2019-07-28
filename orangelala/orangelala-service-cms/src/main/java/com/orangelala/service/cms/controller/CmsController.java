package com.orangelala.service.cms.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orangelala.framework.api.ICmsController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.cms.request.CmsJoinRequest;
import com.orangelala.framework.common.cms.request.QueryCmsTemplateListRequest;
import com.orangelala.framework.common.cms.request.QueryPageListRequest;
import com.orangelala.framework.common.cms.response.AddCmsConfigResponse;
import com.orangelala.framework.common.cms.response.QueryCmsTemplateListResponse;
import com.orangelala.framework.common.cms.response.QueryPageListResponse;
import com.orangelala.framework.common.cms.response.UploadTemplateResponse;
import com.orangelala.framework.common.cms.response.code.CmsCode;
import com.orangelala.framework.common.cms.response.msg.CmsMsg;
import com.orangelala.framework.model.cms.CmsConfig;
import com.orangelala.framework.model.cms.CmsPage;
import com.orangelala.framework.model.cms.CmsTemplate;
import com.orangelala.service.cms.config.RabbitMqConfig;
import com.orangelala.service.cms.service.CmsConfigService;
import com.orangelala.service.cms.service.CmsPageService;
import com.orangelala.service.cms.service.CmsTemplateService;
import com.orangelala.service.cms.service.PageBuildService;

@RestController
@RequestMapping("/cms")
public class CmsController implements ICmsController {
	
	@Autowired
	private CmsPageService cmsPageService;
	@Autowired
	private CmsConfigService cmsConfigService;
	@Autowired
	private PageBuildService pageBuildService;
	@Autowired
	private CmsTemplateService cmsTemplateService;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	//添加页面
	@Override
	@PostMapping("/cms_page/add")
	public BaseResponse addCmsPage(CmsPage cmsPage) {
		
		return cmsPageService.addCmsPage(cmsPage);
	}
	
	//查询页面列表
	@Override
	@GetMapping("/cms_page/list")
	public QueryPageListResponse queryPageList(QueryPageListRequest request) {
		
		return cmsPageService.queryPageList(request);
	}
	
	//根据页面id查询页面
	@Override
	@GetMapping("/cms_page/find")
	public CmsPage queryCmsPageById(String pageId) {
		
		return cmsPageService.findById(pageId);
	}
	
	//添加页面数据
	@Override
	@PostMapping("/cms_config/add")
	public AddCmsConfigResponse addCmsConfig(CmsConfig cmsConfig) {
		
		return cmsConfigService.addCmsConfig(cmsConfig);
	}
	
	//数据模型
	@Override
	@GetMapping("/model/{cmsConfigId}")
	public CmsConfig getModelData(@PathVariable("cmsConfigId")String cmsConfigId) {
		
		return cmsConfigService.findById(cmsConfigId);
	}
	
	//添加模板
	@Override
	@PostMapping("/cms_template/add")
	public BaseResponse addCmsTemplate(CmsTemplate cmsTemplate) {
		
		return cmsTemplateService.addCmsTemplate(cmsTemplate);
	}
	
	//查询模板列表
	@Override
	@GetMapping("/cms_template/list")
	public QueryCmsTemplateListResponse queryCmsTemplateList(QueryCmsTemplateListRequest request) {
		
		return null;
	}
	
	//根据模板id查询
	@Override
	@GetMapping("/cms_template/find")
	public CmsTemplate queryCmsTemplateById(String templateId) {
		
		return cmsTemplateService.findById(templateId);
	}
	
	//页面关联数据和模板
	@Override
	@PostMapping("/join")
	public BaseResponse CmsConfigOrTemplateJoinOnCmsPage(CmsJoinRequest joinRequest) {
		try {
			BaseResponse response = pageBuildService.join(joinRequest);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResponse(CmsCode.SYSTEM_ERROR,CmsMsg.SYSTEM_ERROR);
		}
	}
	
	//上传页面模板
	@Override
	@PostMapping("/cms_template/upload")
	public UploadTemplateResponse uploadTemplate(MultipartFile uploadFile) {
		
		return cmsTemplateService.uploadTemplate(uploadFile);
	}
	
	//页面发布
	@Override
	@GetMapping("/publish")
	public BaseResponse publishPage(String pageId) {
		BaseResponse res = pageBuildService.saveHtml(pageId);
		if(res.getCode() == Code.SUCCESS) {
			//异步通知cms client端生成html文件,推送到nginx服务器
			try {
				rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM, "inform.all.cms.all", pageId);
			} catch (AmqpException e) {
				e.printStackTrace();
				return new BaseResponse(CmsCode.SYSTEM_ERROR,CmsMsg.SYSTEM_ERROR);
			}
		}
		return res;
	}

}
