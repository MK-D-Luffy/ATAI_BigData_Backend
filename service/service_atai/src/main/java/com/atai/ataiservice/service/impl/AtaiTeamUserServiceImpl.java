package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.entity.AtaiTeamUser;
import com.atai.ataiservice.mapper.AtaiTeamUserMapper;
import com.atai.ataiservice.service.AtaiTeamUserService;
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
 * @since 2023-04-09
 */
@Service
public class AtaiTeamUserServiceImpl extends ServiceImpl<AtaiTeamUserMapper, AtaiTeamUser> implements AtaiTeamUserService {

    @Override
    public List<AtaiTeamUser> getUsersByTCId(String teamId, String competitionId) {
        QueryWrapper<AtaiTeamUser> wrapper = new QueryWrapper<>();
        wrapper.eq("team_id", teamId);
        wrapper.eq("competition_id", competitionId);
        wrapper.orderByDesc("is_leader");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public AtaiTeamUser getTeamByCUId(String competitionId, String userId) {
        QueryWrapper<AtaiTeamUser> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id", competitionId);
        wrapper.eq("user_id", userId);
        return baseMapper.selectOne(wrapper);
    }

//    @Override
//    public AtaiTeamUser getByUCId(String userId, String competitionId) {
//        QueryWrapper<AtaiTeamUser> wrapper = new QueryWrapper<>();
//        wrapper.eq("competition_id", competitionId);
//        wrapper.eq("user_id", userId);
//        return  baseMapper.selectOne(wrapper);
//    }

    @Override
    public AtaiTeamUser getUsersByUCId(String userId, String competitionId) {
        QueryWrapper<AtaiTeamUser> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id", competitionId);
        wrapper.eq("user_id", userId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<AtaiTeamUser> getUsersByTeamId(String teamId) {
        QueryWrapper<AtaiTeamUser> wrapper = new QueryWrapper<>();
        wrapper.eq("team_id", teamId);
        return baseMapper.selectList(wrapper);
    }
}
