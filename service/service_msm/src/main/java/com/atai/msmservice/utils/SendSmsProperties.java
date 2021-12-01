package com.atai.msmservice.utils;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯云短信常量类，读取配置文件application.properties中的配置
 *
 * @author baiyun
 */
@Data
@Component
@ConfigurationProperties (prefix = "tencentcloud.sms")
public class SendSmsProperties {
    private String secretId;
    private String secretKey;
    private String appId;

    private String sign;
    private String templateId;

}