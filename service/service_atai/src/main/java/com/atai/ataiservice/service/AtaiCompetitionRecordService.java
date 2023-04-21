package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiCompetitionRecord;
import com.atai.ataiservice.entity.frontvo.CompetitionRecordFrontVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-13
 */
public interface AtaiCompetitionRecordService extends IService<AtaiCompetitionRecord> {

    List<AtaiCompetitionRecord> getRecordByUserId(String userId);

    List<CompetitionRecordFrontVo> getRecordByTeamId(String teamId);
}
