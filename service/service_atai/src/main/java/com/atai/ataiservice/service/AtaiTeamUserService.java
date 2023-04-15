package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiTeamUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
public interface AtaiTeamUserService extends IService<AtaiTeamUser> {

    List<AtaiTeamUser> getUsersByTCId(String teamId, String competitionId);

    AtaiTeamUser getTeamByCUId(String competitionId, String userId);

//    AtaiTeamUser getByUCId(String userId, String competitionId);

    AtaiTeamUser getUsersByUCId(String userId, String competitionId);

    List<AtaiTeamUser> getUsersByTeamId(String teamId);

    List<AtaiTeamUser> getCompetitionByUserId(String userId);
}
