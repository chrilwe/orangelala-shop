package com.orangelala.service.cms.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.cms.response.AddCmsConfigResponse;
import com.orangelala.framework.model.cms.CmsConfig;
import com.orangelala.framework.model.cms.CmsConfigModel;
import com.orangelala.framework.utils.GenerateNum;
import com.orangelala.service.cms.dao.CmsConfigDao;

@Service
public class CmsConfigService {
	
	@Autowired
	private CmsConfigDao cmsConfigDao;
	
	//添加
	public AddCmsConfigResponse addCmsConfig(CmsConfig cmsConfig) {
		if(cmsConfig == null) {
			return new AddCmsConfigResponse(Code.PARAM_NULL,Msg.PARAM_NULL,"");
		}
		String id = cmsConfig.getId();
		CmsConfigModel model = cmsConfig.getModel();
		if(model == null) {
			return new AddCmsConfigResponse(Code.PARAM_NULL,Msg.PARAM_NULL,"");
		}
		if(StringUtils.isEmpty(id)) {
			GenerateNum num = new GenerateNum();
			id = num.generate();
			cmsConfig.setId(id);
		}
		CmsConfig c = cmsConfigDao.insert(cmsConfig);
		return new AddCmsConfigResponse(Code.SUCCESS,Msg.SUCCESS,id);
	}
	
	//删除
	public BaseResponse deleteCmsConfig(String id) {
		if(StringUtils.isEmpty(id)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsConfigDao.deleteById(id);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//更新
	public BaseResponse updateCmsConfig(CmsConfig cmsConfig) {
		if(cmsConfig == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsConfigDao.save(cmsConfig);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//根据id查询
	public CmsConfig findById(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		Optional<CmsConfig> optional = cmsConfigDao.findById(id);
		return optional.get();
	}
}
