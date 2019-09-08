package com.orangelala.service.order.singleton;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

/**
 * 单例自定义线程池
 * @author chrilwe
 *
 */
@Component
public class SingletonExecutors {
	private ThreadPoolExecutor executor;
	private int corePoolSize;
	private int maximumPoolSize;
	private long keepAliveTime;
	
	public SingletonExecutors() {
		ArrayBlockingQueue queue = new ArrayBlockingQueue(corePoolSize);
		executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,queue);
	}
	
	public ThreadPoolExecutor get() {
		return this.executor;
	}
}
