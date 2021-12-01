package com.atai.msmservice.service.impl;

import com.atai.msmservice.service.MsmService;
import com.atai.msmservice.utils.SendSmsProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MsmServiceImpl implements MsmService {

    @Autowired
    private SendSmsProperties sendSmsProperties;

    //发送短信的方法
    @Override
    public boolean send(String[] param, String phoneNumber) {
        // 工具类获取值
        String secretId = sendSmsProperties.getSecretId();
        String secretKey = sendSmsProperties.getSecretKey();
        String appId = sendSmsProperties.getAppId();
        String sign = sendSmsProperties.getSign();
        String templateId = sendSmsProperties.getTemplateId();

        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }

        try {
            // 实例化一个认证对象
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个客户端配置对象
            ClientProfile clientProfile = new ClientProfile();
            // 实例化 SMS 的 client 对象
            SmsClient client = new SmsClient(cred, "", clientProfile);
            // 实例化一个请求对象
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppid(appId);
            req.setSign(sign);
            req.setTemplateID(templateId);
            req.setPhoneNumberSet(new String[]{"+86" + phoneNumber});
            req.setTemplateParamSet(param);
            // 发送请求
            SendSmsResponse res = client.SendSms(req);

            SendStatus[] status = res.getSendStatusSet();
            return "Ok".equals(status[0].getCode());
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return false;
        }

    }
}
