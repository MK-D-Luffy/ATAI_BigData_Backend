package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiCompetitionTeam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
public interface AtaiCompetitionTeamService extends IService<AtaiCompetitionTeam> {

    Map<String, Object> getTeamPageList(Page<AtaiCompetitionTeam> teamPage, String name);

    List<AtaiCompetitionTeam> getRankList(String competitionId);
}
