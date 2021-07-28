package com.atai.competition.client;

import com.atai.commonutils.result.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient("service-oss")
public interface OssClient {
    //根据url获取结果集
    @PostMapping ("/eduoss/fileoss/get")
    public R getFile(@RequestBody () String url);

    @DeleteMapping("/eduoss/fileoss/remove")
    public R removeFile(
            @ApiParam(value = "要删除的文件url路径", required = true)
            @RequestBody String url);
}
