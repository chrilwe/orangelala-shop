package com.orangelala.framework.api;
/**
 * 网站页面管理
 * @author chrilwe
 *
 */

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.cms.request.CmsJoinRequest;
import com.orangelala.framework.common.cms.request.QueryCmsTemplateListRequest;
import com.orangelala.framework.common.cms.request.QueryPageListRequest;
import com.orangelala.framework.common.cms.response.AddCmsConfigResponse;
import com.orangelala.framework.common.cms.response.QueryCmsTemplateListResponse;
import com.orangelala.framework.common.cms.response.QueryPageListResponse;
import com.orangelala.framework.common.cms.response.UploadTemplateResponse;
import com.orangelala.framework.model.cms.CmsConfig;
import com.orangelala.framework.model.cms.CmsPage;
import com.orangelala.framework.model.cms.CmsTemplate;

public interface ICmsController {
	//添加页面
	public BaseResponse addCmsPage(CmsPage cmsPage);
	//页面列表
	public QueryPageListResponse queryPageList(QueryPageListRequest request);
	//根据页面id查询
	public CmsPage queryCmsPageById(String pageId);
	//添加页面数据模型
	public AddCmsConfigResponse addCmsConfig(CmsConfig cmsConfig);
	//获取数据模型
	public CmsConfig getModelData(String id);
	//添加页面模板
	public BaseResponse addCmsTemplate(CmsTemplate cmsTemplate);
	//页面模板列表
	public QueryCmsTemplateListResponse queryCmsTemplateList(QueryCmsTemplateListRequest request);
	//根据模板id查询模板
	public CmsTemplate queryCmsTemplateById(String templateId);
	//页面cmspage关联数据模型和template
	public BaseResponse CmsConfigOrTemplateJoinOnCmsPage(CmsJoinRequest joinRequest);
	//上传页面模型
	public UploadTemplateResponse uploadTemplate(MultipartFile uploadFile);
	//页面发布
	public BaseResponse publishPage(String pageId);
}
