package com.atai.ataiservice.client.fallback;

import com.atai.ataiservice.client.UcenterClient;
import com.atai.commonutils.ordervo.UcenterMemberOrder;
import org.springframework.stereotype.Component;

@Component
public class UcenterFileDegradeFeignClient implements UcenterClient {
    @Override
    public UcenterMemberOrder getUserInfoOrder(String id) {
        return null;
    }
}
