package com.orangelala.service.cms.service;


import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mongodb.client.gridfs.GridFSBucket;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.cms.request.CmsJoinRequest;
import com.orangelala.framework.common.cms.response.CreateHtmlResponse;
import com.orangelala.framework.common.cms.response.GetHtmlResponse;
import com.orangelala.framework.common.cms.response.code.CmsCode;
import com.orangelala.framework.common.cms.response.msg.CmsMsg;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.cms.CmsConfig;
import com.orangelala.framework.model.cms.CmsConfigModel;
import com.orangelala.framework.model.cms.CmsPage;
import com.orangelala.framework.model.cms.CmsSite;
import com.orangelala.framework.model.cms.CmsTemplate;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态页面服务
 * 
 * @author chrilwe
 *
 */
@Service
public class PageBuildService {

	@Autowired
	private CmsPageService cmsPageService;
	@Autowired
	private CmsConfigService cmsConfigService;
	@Autowired
	private CmsTemplateService cmsTemplateService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Autowired
	private GridFSBucket gridFSBucket;
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private CmsSiteService cmsSiteService;

	// 生成html,并且返回html内容
	// 参数: 页面id
	public CreateHtmlResponse createHtml(String pageId) {
		CmsPage cmsPage = cmsPageService.findById(pageId);
		if (cmsPage == null)
			return new CreateHtmlResponse(Code.PARAM_NULL, Msg.PARAM_NULL, "", "");
		// 数据模型获取地址
		String dataUrl = cmsPage.getDataUrl();
		if (StringUtils.isEmpty(dataUrl))
			return new CreateHtmlResponse(CmsCode.DATA_MODEL_NULL, CmsMsg.DATA_MODEL_NULL, "", "");
		// 模板id
		String templateId = cmsPage.getTemplateId();
		if (StringUtils.isEmpty(templateId))
			return new CreateHtmlResponse(CmsCode.TEMPLATE_NULL, CmsMsg.TEMPLATE_NULL, "", "");
		// 获取模型数据
		Map pageDataModel = getPageDataModel(dataUrl);
		if (pageDataModel == null)
			return new CreateHtmlResponse(CmsCode.DATA_MODEL_NULL, CmsMsg.DATA_MODEL_NULL, "", "");
		// 获取页面模板
		CmsTemplate cmsTemplate = cmsTemplateService.findById(templateId);
		if (cmsTemplate == null)
			return new CreateHtmlResponse(CmsCode.TEMPLATE_NULL, CmsMsg.TEMPLATE_NULL, "", "");
		String templateFileId = cmsTemplate.getTemplateFileId();
		String template = cmsTemplateService.getTemplate(templateFileId);
		//页面利用freemarker静态化
		String html = "";
		try {
			html = staticPage(pageDataModel,template);
		} catch (Exception e) {
			e.printStackTrace();
			return new CreateHtmlResponse(Code.SYSTEM_ERROR, Msg.SYSTEM_ERROR, "", "");
		}
		return new CreateHtmlResponse(Code.SUCCESS,Msg.SUCCESS,pageId,html);
	}

	// 页面静态化
	private String staticPage(Map model, String template) throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 模板加载器
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		templateLoader.putTemplate("template", template);
		configuration.setTemplateLoader(templateLoader);
		// 获取模板
		Template t = configuration.getTemplate("template");
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		return html;
	}

	// 保存页面到数据库
	public BaseResponse saveHtml(String pageId) {
		// 查询是否已经保存
		CmsPage cmsPage = cmsPageService.findById(pageId);
		String htmlFileId = cmsPage.getHtmlFileId();
		if (StringUtils.isEmpty(htmlFileId))
			return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
		// 获取html页面数据
		CreateHtmlResponse createHtml = createHtml(pageId);
		if (createHtml.getCode() == Code.SUCCESS) {
			String html = createHtml.getHtml();
			//将HTML保存到gfs
			ByteArrayInputStream stream = new ByteArrayInputStream(html.getBytes());
			ObjectId store = gridFsTemplate.store(stream, cmsPage.getPageName());
			String fileId = store.toString();
			
			//更新cmspage
			cmsPage.setHtmlFileId(htmlFileId);
			cmsPageService.updateCmsPage(cmsPage);
			return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
		}

		return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
	}

	// 获取页面数据模型
	private Map getPageDataModel(String dataUrl) {
		try {
			ResponseEntity<CmsConfig> res = restTemplate.getForEntity(dataUrl, CmsConfig.class);
			int statusCodeValue = res.getStatusCodeValue();
			if (statusCodeValue == Code.SUCCESS) {
				CmsConfig cmsConfig = res.getBody();
				if (cmsConfig != null) {
					CmsConfigModel model = cmsConfig.getModel();
					Map mapValue = model.getMapValue();
					return mapValue;
				}
			}
		} catch (RestClientException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	// 页面关联模板和数据模型
	public BaseResponse join(CmsJoinRequest joinRequest) {
		if (joinRequest == null)
			return new BaseResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		String pageId = joinRequest.getPageId();
		String cmsConfigId = joinRequest.getCmsConfigId();
		String templateId = joinRequest.getTemplateIId();
		String siteId = joinRequest.getSiteId();
		if (StringUtils.isEmpty(pageId) || StringUtils.isEmpty(templateId) || StringUtils.isEmpty(cmsConfigId))
			return new BaseResponse(Code.PARAM_NULL, Msg.PARAM_NULL);
		CmsPage cmsPage = cmsPageService.findById(pageId);
		CmsConfig cmsConfig = cmsConfigService.findById(cmsConfigId);
		CmsTemplate cmsTemplate = cmsTemplateService.findById(templateId);
		CmsSite cmsSite = cmsSiteService.findById(siteId);
		if (cmsPage == null)
			return new BaseResponse(CmsCode.PAGE_NOT_EXISTS, CmsMsg.PAGE_NOT_EXISTS);
		else if (cmsConfig == null)
			return new BaseResponse(CmsCode.DATA_MODEL_NULL, CmsMsg.DATA_MODEL_NULL);
		else if(cmsTemplate == null)
			return new BaseResponse(CmsCode.TEMPLATE_NULL,CmsMsg.TEMPLATE_NULL);
		else if (cmsSite == null)
			return new BaseResponse(CmsCode.SITE_NULL,CmsMsg.SITE_NULL);

		ServiceInstance choose = loadBalancerClient.choose(ServiceList.orangelala_service_cms);
		String dataUrl = choose.getUri() + "/model/" + cmsConfigId;
		cmsPage.setDataUrl(dataUrl);
		cmsPage.setTemplateId(templateId);
		cmsPage.setSiteId(siteId);
		cmsPageService.updateCmsPage(cmsPage);
		return new BaseResponse(CmsCode.SUCCESS, CmsMsg.SUCCESS);
	}
}
