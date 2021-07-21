package com.atai.compentition.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atai.commonutils.result.R;
import com.atai.compentition.client.OssClient;
import com.atai.compentition.entity.AtaiUserCompetition;
import com.atai.compentition.entity.excel.CompeletionResult;
import com.atai.compentition.entity.frontVo.MyCompentition;
import com.atai.compentition.entity.vo.RankingQuery;
import com.atai.compentition.entity.vo.TeamCompetition;
import com.atai.compentition.listener.CompetitionExcelListener;
import com.atai.compentition.mapper.AtaiUserCompetitionMapper;
import com.atai.compentition.service.AtaiCompetitionService;
import com.atai.compentition.service.AtaiUserCompetitionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户比赛 服务实现类
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-18
 */
@Service
public class AtaiUserCompetitionServiceImpl extends ServiceImpl<AtaiUserCompetitionMapper, AtaiUserCompetition> implements AtaiUserCompetitionService {
    @Autowired
    private OssClient ossClient;

    @Autowired
    private AtaiCompetitionService ataiCompetitionService;

    //根据比赛id，用户id查询信息
    @Override
    public AtaiUserCompetition getByUseridCompetitionId(String userId, String compentitionId) {
//        调用mapper接口方法
        AtaiUserCompetition ataiUserCompetition = baseMapper.getMapperByUseridCompetitionId(userId, compentitionId);
        return ataiUserCompetition;
    }

    //2.注册：根据比赛id，用户id插入团队id，团队名称
    @Override
    public boolean insertByUseridCompetitionId(String id, String userId, String compentitionId, String team_name, String team_id, Date date) {
        Boolean flag = baseMapper.insertMapperByUseridCompetitionId(id, userId, compentitionId, team_id, team_name, date);
        return flag;
    }


    @Override
    public boolean deleteByUseridCompetitionId(String userId, String compentitionId) {
        Boolean flag = baseMapper.deleteMapperByUseridCompetitionId(userId, compentitionId);
        return flag;
    }

    @Override
    public String getMax() {
        String maxId = baseMapper.getMax();
        return maxId;
    }

    @Override
    public TeamCompetition[] getTeamCompetition(String compentitionId, String teamId) {
        // 调用mapper接口方法
        TeamCompetition[] teamCompetition = baseMapper.getMapperByTeamidCompetitionId(compentitionId, teamId);
        return teamCompetition;
    }

    @Override
    public void saveResult(MultipartFile multipartFile, AtaiUserCompetitionService ataiUserCompetitionService, String userId, String competitionId) {
        //根据比赛id获取结果集url
        String url = ataiCompetitionService.getById(competitionId).getResult();
        //读取map，判断本地是否存在，不存在去oss上拉取
        R resData = ossClient.downloadOssFile(url);
        try {
            //文件输入流
            InputStream in = multipartFile.getInputStream();
            //调用方法进行读取,官方结果集

            String fileName = multipartFile.getOriginalFilename();
            assert fileName != null;
            if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                // 读取map集合
                Map tarResult = (Map) resData.getData().get("resMap");
                EasyExcel.read(in, CompeletionResult.class, new CompetitionExcelListener(ataiUserCompetitionService, tarResult, userId, competitionId)).sheet().doRead();
            } else if (fileName.endsWith(".txt")) {
                // 读取list集合
                List resList = (List) resData.getData().get("resList");
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                double score = 0;
                for (int i = 0; i < resList.size(); i++) {
                    if ((line = br.readLine()) != null) {
                        System.out.println("line = " + line);
                        if (line.equals(resList.get(i))) {
                            score++;
                        }
                    } else {
                        System.out.println("LastLine = " + line);
                        break;
                    }
                }
                double newScore = (score / resList.size()) * 100.00;
                BigDecimal format = new BigDecimal(newScore);
                newScore = format.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                AtaiUserCompetition ataiUserCompetition = ataiUserCompetitionService.getByUseridCompetitionId(userId, competitionId);
                double oldScore = ataiUserCompetition.getScore();
                Date date = ataiUserCompetition.getDeadline();
                //如果得分更高,则更新成绩提交时间
                if (newScore > oldScore) {
                    date = new Date(System.currentTimeMillis());
                }
                Integer submitCounts = ataiUserCompetition.getSubmitCounts();
                submitCounts--;
                ataiUserCompetitionService.updateByUseridCompetitionId(userId, competitionId, newScore, date, submitCounts);
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateByUseridCompetitionId(String userId, String compentitionId, double score, Date date, int submitCounts) {
        Boolean flag = baseMapper.updateMapperByUseridCompetitionId(userId, compentitionId, score, date, submitCounts);
        return flag;
    }

    @Override
    public List<RankingQuery> getRanking(String compentitionId) {
        List<RankingQuery> rankingQuery = baseMapper.getMapperRanking(compentitionId);
        return rankingQuery;
    }

    @Override
    public List<MyCompentition> getMyCompetitionList(String userId) {
        List<MyCompentition> data = baseMapper.getMyCompetitionList(userId);
        return data;
    }

    @Override
    public Set<String> searchTeamsByKey(String compentitionId, String key) {
        Set<String> teams = baseMapper.searchTeamsByKey(compentitionId, key);
        return teams;
    }
}
