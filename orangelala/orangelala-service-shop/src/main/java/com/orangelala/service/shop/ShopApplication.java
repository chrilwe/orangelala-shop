package com.orangelala.service.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages="com.orangelala.service.shop.mapper")
@EnableDiscoveryClient
public class ShopApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ShopApplication.class, args);
	}

}
