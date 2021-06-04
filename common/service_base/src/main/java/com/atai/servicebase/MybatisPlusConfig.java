package com.atai.servicebase;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    /**
     *   mybatis-plus分页插件
     */
    @Bean
    public com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor paginationInterceptor() {
        com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }
}