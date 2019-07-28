package com.orangelala.service.cms_client.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

import org.apache.commons.io.IOUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.orangelala.framework.model.cms.CmsPage;
import com.orangelala.service.cms_client.client.CmsClient;
import com.orangelala.service.cms_client.task.TaskBuilder;
import com.rabbitmq.client.Channel;

/**
 * 页面静态化
 * 
 * @author chrilwe
 *
 */
@Service
public class HtmlGenerateService {

	@Autowired
	private CmsClient cmsClient;
	@Autowired
	private GridFSBucket gridFSBucket;
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private ExecutorService executorService;

	@RabbitListener(queues = { "queue_inform_cms" })
	public void queue_inform_protal(String msg, Message message, Channel channel) {
		// 将消息放到队列集合中，让多个线程 去处理生成静态页面任务
		TaskBuilder task = new TaskBuilder(msg);
		executorService.submit(task);
	}

	// 创建HTML文本文件
	public void createHTMLText(String pageId) {
		// 查询页面基本信息
		CmsPage cmsPage = cmsClient.queryCmsPageById(pageId);
		String htmlFileId = cmsPage.getHtmlFileId();
		String pagePhysicalPath = cmsPage.getPagePhysicalPath();
		String pageName = cmsPage.getPageName();
		try {
			GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
			// 打开下载流下载文件
			GridFSDownloadStream stream = gridFSBucket.openDownloadStream(file.getObjectId());
			// 创建gridfsresource
			GridFsResource resource = new GridFsResource(file, stream);
			// 生成HTML文本文件推送到nginx
			InputStream inputStream = resource.getInputStream();
			File htmlText = new File(pagePhysicalPath+pageName + ".html");
			OutputStream outputStream = new FileOutputStream(htmlText);
			IOUtils.copy(inputStream, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
