package com.atai.msmservice.controller;

import com.atai.commonutils.result.R;
import com.atai.commonutils.result.ResultCodeEnum;
import com.atai.commonutils.util.SendMail;
import com.atai.msmservice.service.MsmService;
import com.atai.msmservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Api ("短信服务")
@RestController
@RequestMapping ("/edumsm/msm")
//@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //发送邮件的方法
    @ApiOperation (value = "发送邮件")
    @GetMapping ("sendByEmail/{email}")
    public R sendEmail(@PathVariable String email) {
        //1 从redis获取验证码发送次数，防止频繁发送验证码
        //邮箱设置5分钟发3次
        String countStr = redisTemplate.opsForValue().get(email + "Count");
        int count = 0;

        if (!StringUtils.isEmpty(countStr)) {
            count = Integer.parseInt(countStr);
            if (count >= 3) {
                return R.error()
                        .code(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL.getCode())
                        .message(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL.getMessage());
            }
        }
        count++;

        //2 如果redis获取 不到，进行邮件发送
        //生成随机值
        String code = RandomUtil.getSixBitRandom();
        String senderName = "ATAIEmail";
        String subject = "ATAI大数据竞赛平台";
        String content = "本次验证码为:" + code + ",验证码有效时间为5分钟。";
        //调用发送邮件的方法
        try {
            SendMail.senEmail(email, senderName, email, subject, content);
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            //设置一分钟发送次数(防止频繁发送)
            redisTemplate.opsForValue().set(email + "Count", count + "", 1, TimeUnit.MINUTES);
            return R.success();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message("邮件发送失败");
        }
    }


    //发送短信的方法
    @ApiOperation (value = "发送短信")
    @GetMapping ("send/{phoneNumber}")
    public R sendMsm(@PathVariable String phoneNumber) {

        //2 如果redis获取 不到，进行阿里云发送
        //生成随机值，传递阿里云进行发送
        String code = RandomUtil.getSixBitRandom();
        String[] param = {code, "5"};

        //调用service发送短信的方法
        boolean isSend = msmService.send(param, phoneNumber);
        if (isSend) {
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(phoneNumber, code, 5, TimeUnit.MINUTES);
            return R.success();
        } else {
            return R.error().message("发送失败:短信发送过于频繁或内部出现错误");
        }
    }
}
