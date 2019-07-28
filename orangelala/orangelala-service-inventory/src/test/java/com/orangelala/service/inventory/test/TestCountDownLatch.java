package com.orangelala.service.inventory.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestCountDownLatch {
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch cdl = new CountDownLatch(1);
		Runner runner = new Runner(cdl);
		runner.run();
		cdl.await();
		System.out.println("hello"+cdl.getCount());
	}
	
	public static class Runner implements Runnable {
		private CountDownLatch cdl;
		public Runner(CountDownLatch cdl) {
			this.cdl = cdl;
		}

		@Override
		public void run() {
			cdl.countDown();
			cdl.countDown();
		}
		
	}
}
