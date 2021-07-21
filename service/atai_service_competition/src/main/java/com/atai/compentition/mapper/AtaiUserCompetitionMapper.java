package com.atai.compentition.mapper;

import com.atai.compentition.entity.AtaiUserCompetition;
import com.atai.compentition.entity.frontVo.MyCompentition;
import com.atai.compentition.entity.vo.RankingQuery;
import com.atai.compentition.entity.vo.TeamCompetition;
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

    AtaiUserCompetition getMapperByUseridCompetitionId(String userId, String compentitionId);

    Boolean insertMapperByUseridCompetitionId(String id, String userId, String compentitionId, String teamId,String teamName,Date date);

    Boolean updateMapperByUseridCompetitionId(String userId, String compentitionId, double score, Date date, int submitCounts);

    Boolean deleteMapperByUseridCompetitionId(String userId, String compentitionId);

    String getMax();

    TeamCompetition[] getMapperByTeamidCompetitionId(String compentitionId, String teamId);

    List<RankingQuery> getMapperRanking(String compentitionId);

    List<MyCompentition> getMyCompetitionList(String userId);

    Set<String> searchTeamsByKey(String compentitionId, String key);
}
