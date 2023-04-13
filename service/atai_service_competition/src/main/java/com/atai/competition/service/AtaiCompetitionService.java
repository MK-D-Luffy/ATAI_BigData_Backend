package com.atai.competition.service;

import com.atai.competition.entity.AtaiCompetition;
import com.atai.competition.entity.vo.CompetitionQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 比赛 服务类
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-08
 */
public interface AtaiCompetitionService extends IService<AtaiCompetition> {
    //1   分页查询比赛的方法
    Map<String, Object> getCompetitionPageList(Page<AtaiCompetition> compPage, CompetitionQuery competitionQuery);


}
