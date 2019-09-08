package com.orangelala.service.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**
 * IM通讯系统
 * @author chrilwe
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class ImApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ImApplication.class, args);
	}

}
