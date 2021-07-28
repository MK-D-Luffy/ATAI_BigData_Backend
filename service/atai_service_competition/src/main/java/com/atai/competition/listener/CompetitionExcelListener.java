package com.atai.competition.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atai.competition.entity.AtaiUserCompetition;
import com.atai.competition.entity.excel.CompeletionResult;
import com.atai.competition.service.AtaiUserCompetitionService;
import com.atai.servicebase.exceptionhandler.MSException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author baiyun
 */
public class CompetitionExcelListener extends AnalysisEventListener<CompeletionResult> {
    //手动注入
    public AtaiUserCompetitionService ataiUserCompetitionService;

    List<CompeletionResult> list = new ArrayList<>();
    String userId;
    String competitionId;
    Map tarResult = null;//官方结果集

    public CompetitionExcelListener() {
    }

    public CompetitionExcelListener(AtaiUserCompetitionService ataiUserCompetitionService) {
        this.ataiUserCompetitionService = ataiUserCompetitionService;
    }


    public CompetitionExcelListener(AtaiUserCompetitionService ataiUserCompetitionService, Map tarResult, String userId, String competitionId) {
        this.ataiUserCompetitionService = ataiUserCompetitionService;
        this.tarResult = tarResult;
        this.userId = userId;
        this.competitionId = competitionId;
    }


    //读取excel内容，一行一行进行读取
    @Override
    public void invoke(CompeletionResult compeletionResult, AnalysisContext analysisContext) {
        if (compeletionResult == null) {
            throw new MSException(20001, "文件数据为空");
        }
//        System.out.println(JSON.toJSONString(compeletionResult));
        list.add(compeletionResult);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        int score = 0;
        //获取提交的所有结果
        for (CompeletionResult item : list) {
            // 根据id获取官方结果
            String tmpRes = (String) tarResult.get((int) Double.parseDouble(item.getId()) + "");
            if (((int) Double.parseDouble(item.getResult()) + "").equals(tmpRes)) {
                score += 1;
            }
        }
        //计算分数，入库
        System.out.println("'score:" + score);
        //当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
//        String s = formatter.format(date);
        AtaiUserCompetition ataiUserCompetition = ataiUserCompetitionService.getByUseridCompetitionId(userId, competitionId);
        Integer submitCounts = ataiUserCompetition.getSubmitCounts();
        submitCounts--;
        Boolean flag = ataiUserCompetitionService.updateByUseridCompetitionId(userId, competitionId, score, date, submitCounts);
        System.out.println(flag);
    }
}
