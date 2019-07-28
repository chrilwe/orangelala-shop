package com.orangelala.service.inventory.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orangelala.framework.utils.ZkUtil;

@Configuration
public class ItemInventoryConfiguration {
	
	@Value("${inventory.zkNodes}")
	private String zkNodes;
	
	@Bean
	public ZkUtil zkUtil() {
		return new ZkUtil(zkNodes);
	}
}
