package com.atai.compentition;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient  //nacos注册
@MapperScan("com.atai.compentition.mapper")
@EnableFeignClients  //微服务  服务调用
//@ComponentScan(basePackages = {"com.atai"})
@ComponentScan("com.atai")
public class CompentitionApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompentitionApplication.class,args);
    }
}
