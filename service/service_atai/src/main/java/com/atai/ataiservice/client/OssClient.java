package com.atai.ataiservice.client;

import com.atai.commonutils.result.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient ("service-oss")
public interface OssClient {

    @PostMapping (value = "/ataioss/fileoss", consumes = "multipart/form-data")
    public R uploadOssFile(MultipartFile file);

    @PostMapping ("/ataioss/fileoss/get")
    public R getFile(@RequestBody () String url);

    @DeleteMapping ("/ataioss/fileoss/remove")
    public R removeFile(@ApiParam (value = "要删除的文件url路径", required = true)
                        @RequestBody String url);
}
