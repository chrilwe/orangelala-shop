package com.orangelala.service.cms.service;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.common.base.Msg;
import com.orangelala.framework.common.base.PageBean;
import com.orangelala.framework.common.cms.request.QueryPageListRequest;
import com.orangelala.framework.common.cms.response.QueryPageListResponse;
import com.orangelala.framework.common.cms.status.PageStatus;
import com.orangelala.framework.common.services.ServiceList;
import com.orangelala.framework.model.cms.CmsPage;
import com.orangelala.framework.utils.GenerateNum;
import com.orangelala.service.cms.dao.CmsPageDao;

@Service
public class CmsPageService {
	
	@Autowired
	private CmsPageDao cmsPageDao;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Value("${cms.pageWebPath}")
	private String pageWebPath;
	@Value("${cms.pagePhysicalPath}")
	private String pagePhysicalPath;
	
	//添加
	public BaseResponse addCmsPage(CmsPage cmsPage) {
		if(cmsPage == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		String pageId = cmsPage.getPageId();
		if(StringUtils.isEmpty(pageId)) {
			GenerateNum num = new GenerateNum();
			pageId = num.generate();
		}
		
		cmsPage.setPageCreateTime(new Date());
		cmsPage.setPageStatus(PageStatus.NO_PUBLISH);
		cmsPage.setPagePhysicalPath(pagePhysicalPath);
		cmsPage.setPageWebPath(pageWebPath);
		cmsPageDao.insert(cmsPage);
		return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
	}
	
	//删除
	public BaseResponse deleteCmsPage(String pageId) {
		if(StringUtils.isEmpty(pageId)) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsPageDao.deleteById(pageId);
		return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
	}
	
	//更新
	public BaseResponse updateCmsPage(CmsPage cmsPage) {
		if(cmsPage == null) {
			return new BaseResponse(Code.PARAM_NULL,Msg.PARAM_NULL);
		}
		cmsPageDao.save(cmsPage);
		return new BaseResponse(Code.SUCCESS, Msg.SUCCESS);
	}
	
	//查询列表
	public QueryPageListResponse queryPageList(QueryPageListRequest request) {
		if(request == null) {
			return new QueryPageListResponse(Code.PARAM_NULL,Msg.PARAM_NULL,null);
		}
		String pageName = request.getPageName();
		String pageStatus = request.getPageStatus();
		int page = request.getPage();
		int pageSize = request.getPageSize();
		if(page <= 0) {
			page = 1;
		}
		if(pageSize <= 0) {
			pageSize = 10;
		}
		
		Pageable pageable = PageRequest.of(page, pageSize);
		List<CmsPage> content = null;
		int totalPages = 0;
		//没有查询条件，默认查询前10条数据
		if(StringUtils.isEmpty(pageName) && StringUtils.isEmpty(pageStatus)) {
			Page<CmsPage> findAll = cmsPageDao.findAll(pageable);
			totalPages = findAll.getTotalPages();
			content = findAll.getContent();
		} else {
		//有条件，按照添加查询
			CmsPage cmsPage = new CmsPage();
			StringMatcher stringMatcher = StringMatcher.CONTAINING;
			ExampleMatcher matcher = ExampleMatcher.matching();
			matcher = matcher.withMatcher("pageName", ExampleMatcher.GenericPropertyMatchers.contains().stringMatcher(stringMatcher));
			if(!StringUtils.isEmpty(pageName)) {
				cmsPage.setPageName(pageName);
			}
			if(!StringUtils.isEmpty(pageStatus)) {
				cmsPage.setPageStatus(pageStatus);
			}
			Example<CmsPage> of = Example.of(cmsPage, matcher);
			Page<CmsPage> findAll = cmsPageDao.findAll(of,pageable);
			totalPages = findAll.getTotalPages();
			content = findAll.getContent();
		}
		PageBean pageBean = new PageBean();
		pageBean.setList(content);
		pageBean.setPageSize(pageSize);
		pageBean.setTotalPage(totalPages);
		return new QueryPageListResponse(Code.SUCCESS,Msg.SUCCESS,pageBean);
	}
	
	//根据id查询
	public CmsPage findById(String pageId) {
		if(StringUtils.isEmpty(pageId)) {
			return null;
		}
		Optional<CmsPage> optional = cmsPageDao.findById(pageId);
		CmsPage cmsPage = optional.get();
		return cmsPage;
	}
}
