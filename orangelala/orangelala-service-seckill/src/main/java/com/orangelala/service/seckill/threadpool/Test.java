package com.orangelala.service.seckill.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
	public static void main(String[] args) {
		ThreadPoolExecutor pool1 = ThreadPoolBuilder.build(1, 2, 1000l, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10), "Test");
		ThreadPoolExecutor pool2 = ThreadPoolBuilder.build(1, 2, 1000l, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10), "Test");
		System.out.println(pool1.hashCode()+"----------"+pool2.hashCode());
	}
	
}
