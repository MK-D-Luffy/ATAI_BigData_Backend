package com.atai.oss.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 上传文件服务类
 * @author ZengJinming
 * @time 2020-04-03
 */

public interface OssService{
    //上传头像到oss
    String uploadFile(MultipartFile file,RedisTemplate redisTemplate);

    /**
     * 阿里云oss 文件删除
     * @param url 文件的url地址
     */
    void removeFile(String url);

    String downloadOssFile(String url, RedisTemplate redisTemplate) throws IOException;
}
