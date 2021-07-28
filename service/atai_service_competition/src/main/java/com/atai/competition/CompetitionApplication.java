package com.atai.competition;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author baiyun
 */
@SpringBootApplication
@EnableDiscoveryClient  //nacos注册
@MapperScan ("com.atai.competition.mapper")
@EnableFeignClients  //微服务  服务调用
//@ComponentScan(basePackages = {"com.atai"})
@ComponentScan ("com.atai")
public class CompetitionApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompetitionApplication.class, args);
    }
}
