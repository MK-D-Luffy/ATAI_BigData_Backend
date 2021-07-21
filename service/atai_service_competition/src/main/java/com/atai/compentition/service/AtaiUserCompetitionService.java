package com.atai.compentition.service;

import com.atai.compentition.entity.AtaiUserCompetition;
import com.atai.compentition.entity.frontVo.MyCompentition;
import com.atai.compentition.entity.vo.RankingQuery;
import com.atai.compentition.entity.vo.TeamCompetition;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户比赛 服务类
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-18
 */
public interface AtaiUserCompetitionService extends IService<AtaiUserCompetition> {

    AtaiUserCompetition getByUseridCompetitionId(String userId, String competitionId);

    boolean insertByUseridCompetitionId(String id, String userId, String competitionId, String team_name, String team_id, Date date);

    boolean deleteByUseridCompetitionId(String userId, String competitionId);

    String getMax();

    TeamCompetition[] getTeamCompetition(String competitionId, String teamId);

    void saveResult(MultipartFile file, AtaiUserCompetitionService ataiUserCompetitionService, String userId, String competitionId);

    boolean updateByUseridCompetitionId(String userId, String competitionId, double score, Date date, int submitCounts);

    List<RankingQuery> getRanking(String competitionId);

    List<MyCompentition> getMyCompetitionList(String userId);

    Set<String> searchTeamsByKey(String competitionId, String key);
}
