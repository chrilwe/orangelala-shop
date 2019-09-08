package com.orangelala.service.filesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages="com.orangelala.service.filesystem.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages="com.orangelala.service.filesystem.client")
public class FilesystemApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(FilesystemApplication.class, args);
	}

}
