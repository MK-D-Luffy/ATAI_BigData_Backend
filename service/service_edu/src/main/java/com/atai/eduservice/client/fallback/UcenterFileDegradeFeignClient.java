package com.atai.eduservice.client.fallback;

import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.atai.eduservice.client.UcenterClient;
import org.springframework.stereotype.Component;

@Component
public class UcenterFileDegradeFeignClient implements UcenterClient {
    @Override
    public UcenterMemberOrder getUserInfoOrder(String id) {
        return null;
    }
}
