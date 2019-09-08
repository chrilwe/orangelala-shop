package com.orangelala.service.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages="com.orangelala.service.message.mapper")
@EnableDiscoveryClient
public class MessageApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MessageApplication.class, args);
	}

}
