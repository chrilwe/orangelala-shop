package com.orangelala.service.cms_client.task;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 从容器中获取实例
 * @author chrilwe
 *
 */
@Component
public class BeanUtil implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public <T> T getBean(Class<T> classType) {
		return applicationContext.getBean(classType);
	}
}
