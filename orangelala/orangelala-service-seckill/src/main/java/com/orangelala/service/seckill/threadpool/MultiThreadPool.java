package com.orangelala.service.seckill.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThreadPool {
	
	private static final int coreSize = 100; //核心线程池数量
	private static final int maxSize = 105; //最大线程池数量
	private static final int maxTime = 30; //最大空闲时间(/秒)
	private static final int queueSize = 50; //队列存放将要执行任务的最大数
	private static ThreadPoolExecutor executor = null;
	
	private MultiThreadPool() {
		ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(queueSize);
		executor = new ThreadPoolExecutor(coreSize,maxSize,maxTime,TimeUnit.SECONDS,blockingQueue);
	}
	
	private static class Singleton {
		private static MultiThreadPool pool = null;
		static {
			pool = new MultiThreadPool();
		}
		
		private static MultiThreadPool getInstance() {
			return pool;
		}
	}
	
	public static MultiThreadPool getInstance() {
		return Singleton.getInstance();
	}
	
	public static void execute(Runnable runnable) {
		executor.execute(runnable);
	}
}
