package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiTeamJoin;
import com.atai.ataiservice.entity.AtaiTeamUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-10
 */
public interface AtaiTeamJoinService extends IService<AtaiTeamJoin> {

    Boolean deleteByUCTId(String userId, String competitionId, String teamId);

    List<AtaiTeamUser> getUsersByCTId(String competitionId, String teamId);

    boolean checkIsParticipated(String userId, String teamId);
}
