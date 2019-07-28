package com.orangelala.service.cms_client.task;

import com.orangelala.service.cms_client.service.HtmlGenerateService;

/**
 * 构建任务
 * @author chrilwe
 *
 */
public class TaskBuilder implements Runnable {
	private String pageId;
	public TaskBuilder(String pageId) {
		this.pageId = pageId;
	}

	@Override
	public void run() {
		BeanUtil beanUtil = new BeanUtil();
		HtmlGenerateService service = beanUtil.getBean(HtmlGenerateService.class);
		service.createHTMLText(pageId);
	}
	
}
