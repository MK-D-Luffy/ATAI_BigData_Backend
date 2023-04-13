package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.entity.AtaiTeamJoin;
import com.atai.ataiservice.entity.AtaiTeamUser;
import com.atai.ataiservice.mapper.AtaiTeamJoinMapper;
import com.atai.ataiservice.service.AtaiTeamJoinService;
import com.atai.ataiservice.service.AtaiTeamUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * @since 2023-04-10
 */
@Service
public class AtaiTeamJoinServiceImpl extends ServiceImpl<AtaiTeamJoinMapper, AtaiTeamJoin> implements AtaiTeamJoinService {

    @Autowired
    private AtaiTeamUserService ataiTeamUserService;

    @Override
    public Boolean deleteByUCTId(String userId, String competitionId, String teamId) {
        QueryWrapper<AtaiTeamJoin> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("competition_id", competitionId);
        wrapper.eq("team_id", teamId);
        int delete = baseMapper.delete(wrapper);
        return delete == 1;
    }

    @Override
    public List<AtaiTeamUser> getUsersByCTId(String competitionId, String teamId) {
        QueryWrapper<AtaiTeamJoin> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id", competitionId);
        wrapper.eq("team_id", teamId);
        List<AtaiTeamJoin> list = baseMapper.selectList(wrapper);

        ArrayList<AtaiTeamUser> users = new ArrayList<>();
        for (AtaiTeamJoin teamJoin : list) {
            String userId = teamJoin.getUserId();
            AtaiTeamUser user = ataiTeamUserService.getUsersByUCId(userId, competitionId);
            users.add(user);
        }
        return users;
    }
}
