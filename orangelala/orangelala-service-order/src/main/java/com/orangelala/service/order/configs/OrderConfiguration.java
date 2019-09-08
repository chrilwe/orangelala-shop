package com.orangelala.service.order.configs;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.orangelala.framework.utils.ZkUtil;

@Configuration
public class OrderConfiguration {
	
	@Value("${myPool.coreSize}")
	private int coreSize;
	@Value("${myPool.maxSize}")
	private int maxSize;
	@Value("${myPool.timeout}")
	private long timeout;
	@Value("${myPool.queueSize}")
	private int queueSize;
	@Value("${zookeeper.nodes}")
	private String nodes;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public ThreadPoolExecutor pool() {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(coreSize,maxSize,timeout,TimeUnit.SECONDS,new ArrayBlockingQueue<>(queueSize));
		return pool;
	}
	
	@Bean
	public ZkUtil zkUtil() {
		return new ZkUtil(nodes);
	}
}
