package com.orangelala.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EntityScan(basePackages="com.orangelala.framework.model.cms")
public class CmsApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(CmsApplication.class, args);
	}

}
