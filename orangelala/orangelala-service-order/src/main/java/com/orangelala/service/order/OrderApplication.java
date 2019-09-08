package com.orangelala.service.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages="com.orangelala.service.order.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages="com.orangelala.service.order.client")
public class OrderApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(OrderApplication.class, args);
	}

}
