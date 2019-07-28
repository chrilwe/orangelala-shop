package com.orangelala.service.cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients(basePackages="com.orangelala.service.cms_client.client")
public class CmsClientApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(CmsClientApplication.class, args);
	}

}
