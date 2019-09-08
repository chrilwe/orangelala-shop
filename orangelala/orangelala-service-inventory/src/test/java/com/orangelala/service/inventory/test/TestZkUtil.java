package com.orangelala.service.inventory.test;

import java.util.ArrayList;
import java.util.List;

import com.orangelala.framework.utils.ZkUtil;

public class TestZkUtil {
	public static void main(String[] args) {
		List<ZkUtil> list = new ArrayList<ZkUtil>();
		ZkUtil zkUtil = new ZkUtil("139.155.113.168:2181");
		list.add(zkUtil);
		boolean createNode = zkUtil.createNode("/test", "test");
		System.out.println(createNode);
		
	}
}
