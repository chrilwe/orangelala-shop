package com.orangelala.service.inventory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@MapperScan(basePackages="com.orangelala.service.inventory.mapper")
public class InventoryApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(InventoryApplication.class, args);
	}

}
