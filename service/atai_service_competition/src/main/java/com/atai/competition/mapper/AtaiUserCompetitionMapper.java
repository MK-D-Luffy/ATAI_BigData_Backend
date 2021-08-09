package com.atai.competition.mapper;

import com.atai.competition.entity.AtaiApplyMsg;
import com.atai.competition.entity.AtaiUserCompetition;
import com.atai.competition.entity.TeamName;
import com.atai.competition.entity.frontVo.MyCompetition;
import com.atai.competition.entity.vo.RankingQuery;
import com.atai.competition.entity.vo.TeamCompetition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户比赛 Mapper 接口
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-18
 */
public interface AtaiUserCompetitionMapper extends BaseMapper<AtaiUserCompetition> {

    AtaiUserCompetition getMapperByUseridCompetitionId(String userId, String competitionId);

    Boolean insertMapperByUseridCompetitionId(String id, String userId, String competitionId, String teamId, String teamName, Date date);

    Boolean updateMapperByUseridCompetitionId(String userId, String competitionId, double score, Date date, int submitCounts);

    Boolean deleteMapperByUseridCompetitionId(String userId, String competitionId);

    String getMax();

    TeamCompetition[] getMapperByTeamidCompetitionId(String competitionId, String teamId);

    Boolean updateTeamNameMapperByTeamid(String teamId, String teamName);

    List<RankingQuery> getMapperRanking(String competitionId);

    List<MyCompetition> getMyCompetitionList(String userId);

    Set<TeamName> searchTeamsByKey(String competitionId, String key);

    void insertApplyMessage(AtaiApplyMsg applyMsg);

    List<AtaiApplyMsg> selectSenders(String competitionId, String receiveId);

    void deleteApplyMsg(String userId, String competitionId);

    Integer getMapperApplyCount(String competitionId, String userId);

    Integer getMapperApplyTotalCount(String competitionId, String userId, String receiveId);

    List<AtaiApplyMsg> selectReceivers(String competitionId, String senderId);
}
