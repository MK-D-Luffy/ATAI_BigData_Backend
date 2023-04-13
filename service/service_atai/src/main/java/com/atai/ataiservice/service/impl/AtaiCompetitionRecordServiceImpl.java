package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.entity.AtaiCompetitionRecord;
import com.atai.ataiservice.mapper.AtaiCompetitionRecordMapper;
import com.atai.ataiservice.service.AtaiCompetitionRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-13
 */
@Service
public class AtaiCompetitionRecordServiceImpl extends ServiceImpl<AtaiCompetitionRecordMapper, AtaiCompetitionRecord> implements AtaiCompetitionRecordService {

    @Override
    public List<AtaiCompetitionRecord> getRecordByUserId(String userId) {
        QueryWrapper<AtaiCompetitionRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("gmt_create");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<AtaiCompetitionRecord> getRecordByTeamId(String teamId) {
        QueryWrapper<AtaiCompetitionRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("team_id", teamId);
        wrapper.orderByDesc("gmt_create");
        return baseMapper.selectList(wrapper);
    }
}
