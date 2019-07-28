package com.orangelala.service.price;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@MapperScan(basePackages="com.orangelala.service.price.mapper")
@EnableDiscoveryClient
public class PriceApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(PriceApplication.class, args);
	}

}
