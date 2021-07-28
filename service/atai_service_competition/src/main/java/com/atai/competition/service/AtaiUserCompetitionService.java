package com.atai.competition.service;

import com.atai.competition.entity.AtaiApplyMsg;
import com.atai.competition.entity.AtaiUserCompetition;
import com.atai.competition.entity.TeamName;
import com.atai.competition.entity.frontVo.MyCompetition;
import com.atai.competition.entity.vo.RankingQuery;
import com.atai.competition.entity.vo.TeamCompetition;
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

    List<MyCompetition> getMyCompetitionList(String userId);

    Set<TeamName> searchTeamsByKey(String competitionId, String key);

    Boolean changeTeamName(String competitionId, String oldTeamId, String newTeamName);

    void createTeamName(String competitionId, String userId);

    void applyToJoinTeam(String competitionId, String teamName, String userId);

    List<AtaiApplyMsg> getSenders(String competitionId, String receiveId);

    Boolean addMember(String userId, String competitionId, String newTeamName);

    void refuseMember(String userId, String competitionId);

}
