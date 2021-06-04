package com.atai.eduservice.client;

import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.atai.eduservice.client.fallback.UcenterFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "service-ucenter",fallback = UcenterFileDegradeFeignClient.class) //调用的服务名称
@Component
public interface UcenterClient {
    //根据用户id获取用户信息
    @PostMapping("/eduucenter/ucenter-member/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable("id") String id) ;
}
