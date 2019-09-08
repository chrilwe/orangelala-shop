package com.orangelala.framework.utils;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
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
		try {
			String create = zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			return true;
		} catch (Exception e) {
			//创建锁失败，先判断当前线程持有的锁是否超时,超时需要先删除锁，在进行获取锁
			while(true) {
				String value = this.getData(path);
				if(StringUtils.isNotEmpty(value)) {
					long createTime = Long.parseLong(value);
					long currentTime = System.currentTimeMillis();
					long time = currentTime - createTime;
					if(time > timeLimit) {
						//超时锁,把锁删除
						this.deleteNode(path);
					}
				}
				//创建节点
				try {
					zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
					return true;
				} catch (Exception e1) {
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
			System.out.println(create);
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
	
	/**
	 * 获取存放在节点的数据
	 */
	public String getData(String path) {
		try {
			byte[] data = zooKeeper.getData(path, false, new Stat());
			if(data == null) {
				return null;
			}
			return new String(data);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
