package com.orangelala.service.item.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.cms.response.code.CmsCode;
import com.orangelala.framework.common.cms.response.msg.CmsMsg;
import com.orangelala.framework.common.cms.type.PageType;
import com.orangelala.framework.common.item.response.code.ItemCode;
import com.orangelala.framework.common.item.response.msg.ItemMsg;
import com.orangelala.framework.model.cms.CmsConfig;
import com.orangelala.framework.model.cms.CmsConfigModel;
import com.orangelala.framework.model.cms.CmsPage;
import com.orangelala.framework.model.cms.CmsTemplate;
import com.orangelala.framework.utils.GenerateNum;
import com.orangelala.service.item.client.CmsClient;

/**
 * 商品详情页静态化管理
 * @author chrilwe
 *
 */
@Service
public class ItemDetailPageService {
	
	@Autowired
	private CmsClient cmsClient;
	
	//商品详情页预览
	public BaseResponse previewItemPage(String templateId,String siteId) {
		if(StringUtils.isEmpty(templateId) || StringUtils.isEmpty(siteId)) {
			return new BaseResponse(CmsCode.PARAM_NULL,CmsMsg.PARAM_NULL);
		}
		//创建页面信息CmsPage
		GenerateNum num = new GenerateNum();
		String pageId = num.generate();
		CmsPage cmsPage = new CmsPage();
		cmsPage.setPageId(pageId);
		cmsPage.setPageCreateTime(new Date());
		cmsPage.setPageName(pageId);
		cmsPage.setPageType(PageType.STATIC);
		//创建数据模型
		CmsConfig cmsConfig = new CmsConfig();
		cmsConfig.setId(pageId);
		cmsConfig.setName(pageId);
		CmsConfigModel model = new CmsConfigModel();
		cmsConfig.setModel(model);
		//CmsPage关联数据
		
		//生成html页面字符串
		return null;
	}
	
	//商品发布
	public BaseResponse publishItem(String itemId,String pageId) {
		
		return null;
	}
}
