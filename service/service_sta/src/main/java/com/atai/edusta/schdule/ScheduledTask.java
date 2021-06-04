package com.atai.edusta.schdule;

import com.atai.edusta.service.StatisticsDailyService;
import com.atai.edusta.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.atai.commonutils.util.SendMail;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.atai.commonutils.result.R;

@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //cron表达式  设置时间 spring写6位，不然会报错
    // 在线生成cron表达式  http://cron.qqe2.com/
    //在每天0点，把前一天数据进行数据查询添加
    @Scheduled(cron = "0 0 0 * * ? ")
    public void taskDaily() {
        statisticsDailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }

    //3分钟发送报表邮件
//    @Scheduled(fixedRate = 100*1)
//    public R playSth() {
//        Map<String,Object> param = new HashMap<>();
//        String content = "统计结果";
//        param.put("code",content);
//        String senderName = "ATAI大数据报表订阅";
//        String subject = "报表统计";
//        //从数据库获取报表内容，定时发送给订阅者
//        String email = "1241765589@qq.com";
//        //调用s发送邮件的方法
//        try {
//            //发送邮件
//            SendMail.senEmail(email,senderName,email,subject,content);
//            return R.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.error().message("邮件发送失败");
//        }
//    }

}
