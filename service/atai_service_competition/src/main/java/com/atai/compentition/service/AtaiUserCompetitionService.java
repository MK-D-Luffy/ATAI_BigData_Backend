package com.atai.compentition.service;

import com.atai.compentition.entity.AtaiUserCompetition;
import com.atai.compentition.entity.frontVo.MyCompentition;
import com.atai.compentition.entity.vo.RankingQuery;
import com.atai.compentition.entity.vo.TeamCompetition;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户比赛 服务类
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-18
 */
public interface AtaiUserCompetitionService extends IService<AtaiUserCompetition> {

    AtaiUserCompetition getByUseridCompetitionId(String userId, String compentitionId);

    boolean insertByUseridCompetitionId(String id,String userId, String compentitionId, String team_name, String team_id,Date date);

    boolean deleteByUseridCompetitionId(String userId, String compentitionId);

    String getMax();

    TeamCompetition[] getTeamCompetition(String compentitionId, String teamId);

    void saveResult(MultipartFile file, AtaiUserCompetitionService ataiUserCompetitionService, String userId, String competitionId);

    boolean updateByUseridCompetitionId(String userId, String compentitionId, int score, Date date);

    List<RankingQuery> getRanking(String compentitionId);

    List<MyCompentition> getMyCompetitionList(String userId);
}
