package com.orangelala.service.cms.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.cms.response.UploadTemplateResponse;
import com.orangelala.framework.model.cms.CmsTemplate;
import com.orangelala.service.cms.dao.CmsTemplateDao;

@Service
public class CmsTemplateService {
	
	@Autowired
	private CmsTemplateDao cmsTemplateDao;
	
	//添加
	public BaseResponse addCmsTemplate(CmsTemplate cmsTemplate) {
		if(cmsTemplate == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsTemplateDao.insert(cmsTemplate);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//删除
	public BaseResponse deleteCmsTemplate(String id) {
		if(StringUtils.isEmpty(id)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsTemplateDao.deleteById(id);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//更新
	public BaseResponse updateCmsTemplate(CmsTemplate cmsTemplate) {
		if(cmsTemplate == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsTemplateDao.save(cmsTemplate);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//根据id查询基本信息
	public CmsTemplate findById(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		Optional<CmsTemplate> optional = cmsTemplateDao.findById(id);
		return optional.get();
	}
	
	//从gfs中取出模板数据
	public String getTemplate(String fieldId) {
		
		return null;
	}
	
	
	//上传模板文件
	public UploadTemplateResponse uploadTemplate(MultipartFile uploadFile) {
		
		return null;
	}
}
