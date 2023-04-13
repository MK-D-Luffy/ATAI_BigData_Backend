package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.entity.AtaiCompetitionTeam;
import com.atai.ataiservice.entity.AtaiTeamUser;
import com.atai.ataiservice.entity.frontvo.TeamFrontVo;
import com.atai.ataiservice.mapper.AtaiCompetitionTeamMapper;
import com.atai.ataiservice.service.AtaiCompetitionTeamService;
import com.atai.ataiservice.service.AtaiTeamUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
@Service
public class AtaiCompetitionTeamServiceImpl extends ServiceImpl<AtaiCompetitionTeamMapper, AtaiCompetitionTeam> implements AtaiCompetitionTeamService {

    @Autowired
    private AtaiTeamUserService ataiTeamUserService;

    @Override
    public Map<String, Object> getTeamPageList(Page<AtaiCompetitionTeam> teamPage, String name) {
        QueryWrapper<AtaiCompetitionTeam> wrapper = new QueryWrapper<>();

        //判断条件值是否为空，不为空拼接
        if (!StringUtils.isEmpty((name))) {//关键字
            wrapper.like("name", name);
        }
        //wrapper.ne("id",id);
//        wrapper.eq("is_allowed", 1);

        //把分页数据封装到pageComp对象里去
        baseMapper.selectPage(teamPage, wrapper);

        List<AtaiCompetitionTeam> records = teamPage.getRecords();
        ArrayList<TeamFrontVo> list = new ArrayList<>();
        for (AtaiCompetitionTeam record : records) {
            TeamFrontVo team = new TeamFrontVo();
            BeanUtils.copyProperties(record, team);
            List<AtaiTeamUser> users = ataiTeamUserService.getUsersByTCId(record.getId(), record.getCompetitionId());
            team.setUsers(users);
            list.add(team);
        }

        long current = teamPage.getCurrent();
        long pages = teamPage.getPages();
        long size = teamPage.getSize();
        long total = teamPage.getTotal();
        boolean hasNext = teamPage.hasNext();//下一页
        boolean hasPrevious = teamPage.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", list);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }

    @Override
    public List<AtaiCompetitionTeam> getRankList(String competitionId) {
        QueryWrapper<AtaiCompetitionTeam> wrapper = new QueryWrapper<>();
        wrapper.eq("competition_id", competitionId);
        wrapper.orderByDesc("score");
        return baseMapper.selectList(wrapper);
    }
}
