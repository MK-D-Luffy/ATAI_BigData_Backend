package com.atai.oss.controller;

import com.atai.commonutils.result.R;
import com.atai.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 上传文件部分
 *
 * @author ZengJinming
 * @time 2020-04-03
 */

@Api ("上传文件")
@RestController
@RequestMapping ("/ataioss/fileoss")
//@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    @Autowired
    private RedisTemplate redisTemplate;

    // 上传文件(数据集、结果集和头像)到oss
    @ApiOperation (value = "上传文件(数据集、结果集和头像)的方法")
    @PostMapping (value = "/upload")
    public R uploadOssFile(MultipartFile file) {
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadFile(file, redisTemplate);
        return R.success().data("url", url);
    }


    @ApiOperation (value = "文件删除")
    @DeleteMapping ("remove")
    public R removeFile(
            @ApiParam (value = "要删除的文件url路径", required = true)
            @RequestBody String url) {
        ossService.removeFile(url);
        redisTemplate.delete(url);
        return R.success().message("文件删除成功");
    }


    // 先从Redis中获取结果集,Redis中不存在了去Oss上获取结果集
    @ApiOperation (value = "返回结果集的方法")
    @PostMapping ("get")
    public R getFile(
            @ApiParam (value = "要下载的文件url路径", required = true)
            @RequestBody () String url) throws IOException {

        // 文件在上传时已经存入redis中了,这里直接从redis中获取
        if (url.endsWith(".xls") || url.endsWith(".xlsx")) {
            // 从redis中根据url取文件
            Map entries = redisTemplate.boundHashOps(url).entries();
            return R.success().data("resMap", entries);
        } else if (url.endsWith(".txt")) {
            ArrayList<String> list = (ArrayList<String>) redisTemplate.boundListOps(url).range(0, -1).get(0);
            // 如果Redis中不存在,就去Oss中拉取文件
            if (list == null) {
                list = ossService.getFileFromOss(url);
            }
            return R.success().data("resList", list);
        }
        return null;
    }

}
