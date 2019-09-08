package com.orangelala.service.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan(basePackages="com.orangelala.service.coupon.mapper")
@EnableEurekaClient
@EnableDiscoveryClient
public class CouponApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(CouponApplication.class, args);
	}

}
