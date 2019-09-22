package com.orangelala.service.inventory.configs;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ItemInventoryConfiguration {
	
	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useClusterServers()
		      .addNodeAddress("redis://127.0.0.1:6379");
		return Redisson.create(config);
	}
}
