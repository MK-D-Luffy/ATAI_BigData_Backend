package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.client.UcenterClient;
import com.atai.ataiservice.entity.AtaiCompetitionRecord;
import com.atai.ataiservice.entity.frontvo.CompetitionRecordFrontVo;
import com.atai.ataiservice.mapper.AtaiCompetitionRecordMapper;
import com.atai.ataiservice.service.AtaiCompetitionRecordService;
import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public List<AtaiCompetitionRecord> getRecordByUserId(String userId) {
        QueryWrapper<AtaiCompetitionRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("gmt_create");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<CompetitionRecordFrontVo> getRecordByTeamId(String teamId) {
        QueryWrapper<AtaiCompetitionRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("team_id", teamId);
        wrapper.orderByDesc("gmt_create");
        List<AtaiCompetitionRecord> records = baseMapper.selectList(wrapper);
        ArrayList<CompetitionRecordFrontVo> list = new ArrayList<>();
        for (AtaiCompetitionRecord record : records) {
            CompetitionRecordFrontVo recordFrontVo = new CompetitionRecordFrontVo();
            BeanUtils.copyProperties(record, recordFrontVo);

            String userId = record.getUserId();
            UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(userId);
            String nickname = userInfoOrder.getNickname();
            recordFrontVo.setUsername(nickname);

            list.add(recordFrontVo);
        }
        return list;
    }
}
