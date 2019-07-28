package com.orangelala.service.cms_client.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CmsClientConfiguration {
	
	@Value("${pool.size}")
	private int poolSize;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	//初始化一个单例线程池处理任务
	@Bean
	public ExecutorService executorService() {
		ExecutorService pool = Executors.newFixedThreadPool(poolSize);
		return pool;
	}
}
