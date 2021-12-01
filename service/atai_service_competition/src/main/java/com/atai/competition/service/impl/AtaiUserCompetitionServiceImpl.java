package com.atai.competition.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atai.commonutils.result.R;
import com.atai.competition.client.OssClient;
import com.atai.competition.entity.*;
import com.atai.competition.entity.excel.CompeletionResult;
import com.atai.competition.entity.frontVo.MyCompetition;
import com.atai.competition.entity.vo.RankingQuery;
import com.atai.competition.entity.vo.TeamCompetition;
import com.atai.competition.listener.CompetitionExcelListener;
import com.atai.competition.mapper.AtaiUserCompetitionMapper;
import com.atai.competition.service.AtaiCompetitionService;
import com.atai.competition.service.AtaiUserCompetitionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

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

    @Autowired
    private AtaiUserCompetitionService ataiUserCompetitionService;

    @Autowired
    private AtaiRunCode ataiRunCode;

    //根据比赛id，用户id查询信息
    @Override
    public AtaiUserCompetition getByUseridCompetitionId(String userId, String competitionId) {
//        调用mapper接口方法
        return baseMapper.getMapperByUseridCompetitionId(userId, competitionId);
    }

    //2.注册：根据比赛id，用户id插入团队id，团队名称
    @Override
    public boolean insertByUseridCompetitionId(String id, String userId, String competitionId, String team_name, String team_id, Date date) {
        return baseMapper.insertMapperByUseridCompetitionId(id, userId, competitionId, team_id, team_name, date);
    }


    @Override
    public boolean deleteByUseridCompetitionId(String userId, String competitionId) {
        return baseMapper.deleteMapperByUseridCompetitionId(userId, competitionId);
    }

    @Override
    public String getMax() {
        return baseMapper.getMax();
    }

    @Override
    public TeamCompetition[] getTeamCompetition(String competitionId, String teamId) {
        // 调用mapper接口方法
        TeamCompetition[] teamCompetition = baseMapper.getMapperByTeamidCompetitionId(competitionId, teamId);
        return teamCompetition;
    }

    @Override
    public void saveResult(MultipartFile multipartFile, AtaiUserCompetitionService ataiUserCompetitionService, String userId, String competitionId) {
        //根据比赛id获取结果集url
        String url = ataiCompetitionService.getById(competitionId).getResult();
        //读取数据，先判断Redis是否存在，如果不存在再去Oss上拉取
        R resData = ossClient.getFile(url);
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
                // 读取结果集
                ArrayList<String> resList = (ArrayList<String>) resData.getData().get("resList");

                // 提交的答案
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                double score = 0;
                for (String s : resList) {
                    if ((line = br.readLine()) != null) {
                        if (line.equals(s)) {
                            score++;
                        }
                    } else {
                        break;
                    }
                }

                //算出得分
                double newScore = (score / resList.size()) * 100.00;
                BigDecimal format = new BigDecimal(newScore);
                newScore = format.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                AtaiUserCompetition ataiUserCompetition = ataiUserCompetitionService.getByUseridCompetitionId(userId, competitionId);
                double oldScore = ataiUserCompetition.getScore();
                Date date = ataiUserCompetition.getDeadline();
                //如果得分更高,则更新成绩提交时间
                if (newScore > oldScore) {
                    date = new Date(System.currentTimeMillis());
                } else {
                    //如果得分不如之前,则保存之前的得分
                    newScore = oldScore;
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
    public boolean updateByUseridCompetitionId(String userId, String competitionId, double score, Date date, int submitCounts) {
        return baseMapper.updateMapperByUseridCompetitionId(userId, competitionId, score, date, submitCounts);
    }

    @Override
    public List<RankingQuery> getRanking(String competitionId) {
        return baseMapper.getMapperRanking(competitionId);
    }

    @Override
    public List<MyCompetition> getMyCompetitionList(String userId) {
        return baseMapper.getMyCompetitionList(userId);
    }

    @Override
    public Set<TeamName> searchTeamsByKey(String competitionId, String key) {
        return baseMapper.searchTeamsByKey(competitionId, key);
    }

    @Override
    public Boolean changeTeamName(String competitionId, String oldTeamId, String newTeamName) {
        // 判断团队名是否重复
        QueryWrapper<AtaiUserCompetition> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id", competitionId);
        wrapper.eq("team_name", newTeamName);
        Integer count = baseMapper.selectCount(wrapper);

        if (count > 0) {
            return false;
        } else {
            // 根据比赛id和团队id找到原本对应的比赛列表
            QueryWrapper<AtaiUserCompetition> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("competition_id", competitionId);
            wrapper2.eq("team_id", oldTeamId);
            List<AtaiUserCompetition> list = baseMapper.selectList(wrapper2);

            //修改该团队所有用户的团队名和团队id
            String newTeamId = String.valueOf(newTeamName.hashCode());
            for (AtaiUserCompetition ataiUserCompetition : list) {
                ataiUserCompetition.setTeamId(newTeamId).setTeamName(newTeamName);
                ataiUserCompetitionService.updateById(ataiUserCompetition);
            }
            return true;
        }
    }

    @Override
    public void createTeamName(String competitionId, String userId) {
        QueryWrapper<AtaiUserCompetition> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id", competitionId);
        wrapper.eq("user_id", userId);

        //随机初始化团队名
        String newTeamName = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String newTeamId = String.valueOf(newTeamName.hashCode());

        //修改为删除后的团队名
        AtaiUserCompetition ataiUserCompetition = new AtaiUserCompetition();
        ataiUserCompetition
                .setTeamName(newTeamName)
                .setTeamId(newTeamId)
                .setTeamLevel(1);
        baseMapper.update(ataiUserCompetition, wrapper);
    }

    @Override
    public void applyToJoinTeam(String competitionId, String teamName, String userId) {
        //判断我的队伍是否已超过两人
        AtaiUserCompetition userCompetition = baseMapper.getMapperByUseridCompetitionId(userId, competitionId);
        String name = userCompetition.getTeamName();
        QueryWrapper<AtaiUserCompetition> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("team_name", name);
        Integer count = baseMapper.selectCount(wrapper1);
        if (count > 1) {
            throw new RuntimeException("无法申请,请您先单独成队!");
        }

        //获取接受申请队伍的管理员(level:1)
        QueryWrapper<AtaiUserCompetition> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id", competitionId);
        wrapper.eq("team_name", teamName);
        wrapper.eq("team_level", 1);
        AtaiUserCompetition ataiUserCompetition = baseMapper.selectOne(wrapper);
        String receiveId = ataiUserCompetition.getUserId();

        Integer applyCount = baseMapper.getMapperApplyCount(competitionId, userId);
        if (applyCount == 3) {
            throw new RuntimeException("无法申请,最多同时申请3个队伍!");
        }

        Integer applyTotalCount = baseMapper.getMapperApplyTotalCount(competitionId, userId, receiveId);
        if (applyTotalCount == 1) {
            throw new RuntimeException("无法申请,请勿重复申请");
        }

        //创建申请对象
        AtaiApplyMsg ataiApplyMsg = new AtaiApplyMsg()
                .setId(UUID.randomUUID().toString().replace("-", "").substring(0, 16))
                .setCompetitionId(competitionId)
                .setSenderId(userId)
                .setReceiveId(receiveId);

        baseMapper.insertApplyMessage(ataiApplyMsg);
    }

    @Override
    public List<AtaiApplyMsg> getSenders(String competitionId, String receiveId) {
        //根据比赛id和我的id获取发送给我的申请
        return baseMapper.selectSenders(competitionId, receiveId);
    }

    @Override
    public Boolean acceptMember(String competitionId, String senderId, String userId, String newTeamName) {
        //获取新的队伍名
        AtaiUserCompetition ataiUserCompetition = baseMapper.getMapperByUseridCompetitionId(senderId, competitionId);
        String newTeamId = String.valueOf(newTeamName.hashCode());
        //将成员加入队伍中
        ataiUserCompetition
                .setTeamName(newTeamName)
                .setTeamId(newTeamId)
                .setTeamLevel(0);

        //删除该成员之前的所有申请信息
        baseMapper.deleteApplyMsg(senderId, competitionId);

        //删除我的申请信息
        baseMapper.deleteApplyMsg(userId, competitionId);

        return baseMapper.updateById(ataiUserCompetition) != 0;
    }

    @Override
    public void refuseMember(String senderId, String competitionId) {
        //删除id为(senderId)的用户发出的申请
        baseMapper.deleteApplyMsg(senderId, competitionId);
    }

    @Override
    public List<TeamName> getReceivers(String competitionId, String senderId) {
        //获取我发出的申请
        List<AtaiApplyMsg> applyMsgs = baseMapper.selectReceivers(competitionId, senderId);
        ArrayList<TeamName> list = new ArrayList<>();
        for (AtaiApplyMsg applyMsg : applyMsgs) {
            //获取被申请人的用户id
            String receiveId = applyMsg.getReceiveId();
            AtaiUserCompetition ataiUserCompetition = baseMapper.getMapperByUseridCompetitionId(receiveId, competitionId);
            //根据用户id和比赛id获取比赛名称
            String teamName = ataiUserCompetition.getTeamName();
            //封装为对象返回
            list.add(new TeamName().setTeamName(teamName));
        }
        return list;
    }


    @Override
    public AtaiProcessResult runCode(String type, String code) throws IOException, InterruptedException {
        return ataiRunCode.runCode(type, code);
    }

}
