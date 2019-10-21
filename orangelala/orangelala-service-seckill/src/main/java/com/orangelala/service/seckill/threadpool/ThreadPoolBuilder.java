package com.orangelala.service.seckill.threadpool;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolBuilder {
	
	//创建线程池
	public static ThreadPoolExecutor build(int coreSize,int maxSize,
			long timeout,TimeUnit timeUnit,BlockingQueue<Runnable> queue, String poolName) {
		//从map线程池获取threadpool,没有获取到，实例一个线程池.
		ThreadPoolExecutor tpe = null;
		tpe = (ThreadPoolExecutor) PoolGroups.pools.get(poolName);
		if(tpe == null) {
			tpe = new ThreadPoolExecutor(coreSize,maxSize,timeout,timeUnit,queue);
			PoolGroups.pools.put(poolName, tpe);
		}
		return tpe;
	}
	
}
