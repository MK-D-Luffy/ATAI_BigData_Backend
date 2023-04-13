package com.atai.ataiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient  //nacos注册
@EnableFeignClients  //微服务  服务调用
@ComponentScan (basePackages = {"com.atai"})
public class AtaiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AtaiApplication.class, args);
    }
}
