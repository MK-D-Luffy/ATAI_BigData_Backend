package com.atai.msmservice.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量类，读取配置文件application.properties中的配置
 */
@Component
//@PropertySource("classpath:application.properties")
public class ConstantPropertiesUtil implements InitializingBean {
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.signname}")
    private String signName;

    @Value("${aliyun.oss.code}")
    private String code;


    public static String ACCESS_KEY_ID;

    public static String ACCESS_KEY_SECRET;

    public static String SIGN_NAME;
    public static String CODE;


    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        SIGN_NAME = signName;
        CODE = code;

    }
}