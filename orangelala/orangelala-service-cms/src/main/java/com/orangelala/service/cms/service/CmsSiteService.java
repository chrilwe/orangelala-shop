package com.orangelala.service.cms.service;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.cms.response.code.CmsCode;
import com.orangelala.framework.common.cms.response.msg.CmsMsg;
import com.orangelala.framework.model.cms.CmsSite;
import com.orangelala.framework.model.cms.CmsSiteServer;
import com.orangelala.service.cms.dao.CmsSiteDao;

@Service
public class CmsSiteService {
	
	@Autowired
	private CmsSiteDao cmsSiteDao;
	@Autowired
	private CmsSiteServerService cmsSiteServerService;
	
	//添加
	public BaseResponse addCmsSite(CmsSite cmsSite) {
		if(cmsSite == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsSite.setSiteCreateTime(new Date());
		cmsSiteDao.insert(cmsSite);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//删除
	public BaseResponse deleteCmsSite(String id) {
		if(StringUtils.isEmpty(id)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsSiteDao.deleteById(id);
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//根据id查询
	public CmsSite findById(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		Optional<CmsSite> optional = cmsSiteDao.findById(id);
		return optional.get();
	}
	
	//关联siteServer
	public BaseResponse join(String siteId,String siteServerId) {
		if(StringUtils.isEmpty(siteId) || StringUtils.isEmpty(siteServerId)) {
			return new BaseResponse(CmsCode.PARAM_NULL,CmsMsg.PARAM_NULL);
		}
		CmsSite cmsSite = findById(siteId);
		CmsSiteServer cmsSiteServer = cmsSiteServerService.findById(siteServerId);
		if(cmsSite == null) {
			return new BaseResponse(CmsCode.SITE_NULL,CmsMsg.SITE_NULL);
		} else if(cmsSiteServer == null) {
			return new BaseResponse(CmsCode.SITESERVER_NULL,CmsMsg.SITESERVER_NULL);
		}
		cmsSiteServer.setSiteId(siteId);
		return cmsSiteServerService.updateCmsSiteServer(cmsSiteServer);
	}
}
