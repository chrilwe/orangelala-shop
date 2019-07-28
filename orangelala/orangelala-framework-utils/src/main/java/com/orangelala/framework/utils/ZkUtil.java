package com.orangelala.framework.utils;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;


/**
 * zookeeper分布式锁
 * 使用时将它注入spring即可
 * @author chrilwe
 *
 */
public class ZkUtil {
	
	private ZooKeeper zooKeeper;
	//多线程同步
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	/**
	 * 初始化连接zookeeper
	 */
	public ZkUtil(String zkNodes) {
		try {
			zooKeeper = new ZooKeeper(zkNodes, 60 * 1000, new ZkWatcher());
			//等待连接
			countDownLatch.await();
			System.out.println("-----zookeeper连接成功-----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 监控zookeeper连接状态
	 */
	private class ZkWatcher implements Watcher {

		@Override
		public void process(WatchedEvent event) {
			if(event.getState() == KeeperState.SyncConnected) {
				countDownLatch.countDown();
			}
		}
		
	}
	
	/**
	 * 创建临时节点，相同时间内保证只有一个线程成功创建节点
	 * 参数:
	 * 1.创建节点的路径
	 * 2.节点存放的数据
	 * 3.创建节点的时间限制（毫秒）
	 */
	public boolean createNode(String path,String data,long timeLimit) {
		long startTime = System.currentTimeMillis();
		try {
			String create = zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			return true;
		} catch (Exception e) {
			//创建节点失败,重新创建节点，直到创建节点成功
			while(true) {
				//解决死锁问题
				long nextTime = System.currentTimeMillis();
				if(nextTime - startTime > timeLimit) {
					return false;
				}
				try {
					zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
					return true;
				} catch (Exception e2) {
					continue;
				}
			}
		}
	}
	
	/**
	 * 创建节点，创建节点失败即刻返回
	 * @param path
	 * @param data
	 * @return
	 */
	public boolean createNode(String path,String data) {
		try {
			String create = zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 删除节点
	 */
	public boolean deleteNode(String path) {
		try {
			zooKeeper.delete(path, -1);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
