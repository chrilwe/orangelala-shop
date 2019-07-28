package com.orangelala.service.cms.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.model.cms.CmsSiteServer;
import com.orangelala.service.cms.dao.CmsSiteServerDao;

@Service
public class CmsSiteServerService {
	
	@Autowired
	private CmsSiteServerDao cmsSiteServerDao;
	
	//添加
	public BaseResponse addCmsSiteServer(CmsSiteServer cmsSiteServer) {
		if(cmsSiteServer == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		try {
			cmsSiteServerDao.insert(cmsSiteServer);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	
	//删除
	public BaseResponse deleteCmsSiteServer(String id) {
		if(StringUtils.isEmpty(id)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		try {
			cmsSiteServerDao.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	//更新
	public BaseResponse updateCmsSiteServer(CmsSiteServer cmsSiteServer) {
		if(cmsSiteServer == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		try {
			cmsSiteServerDao.save(cmsSiteServer);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResponse(Code.SYSTEM_ERROR,Msg.SYSTEM_ERROR);
		}
		return new BaseResponse(Code.SUCCESS,Msg.SUCCESS);
	}
	
	
	//根据id查询
	public CmsSiteServer findById(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		CmsSiteServer cmsSiteServer = null;
		try {
			Optional<CmsSiteServer> optional = cmsSiteServerDao.findById(id);
			cmsSiteServer = optional.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmsSiteServer;
	}
}
