package com.atai.competition.controller;


import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.atai.commonutils.result.R;
import com.atai.commonutils.util.JwtInfo;
import com.atai.commonutils.util.JwtUtils;
import com.atai.competition.client.UcenterClient;
import com.atai.competition.entity.AtaiApplyMsg;
import com.atai.competition.entity.AtaiCompetition;
import com.atai.competition.entity.AtaiUserCompetition;
import com.atai.competition.entity.TeamName;
import com.atai.competition.entity.frontVo.MyCompetition;
import com.atai.competition.entity.vo.RankingQuery;
import com.atai.competition.entity.vo.TeamCompetition;
import com.atai.competition.service.AtaiCompetitionService;
import com.atai.competition.service.AtaiUserCompetitionService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * <p>
 * 用户比赛 前端控制器
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-18
 */
@Api (description = "用户比赛管理")
@RestController
@RequestMapping ("/atitcompetition/atai-user-competition")
@Slf4j
public class AtaiUserCompetitionController {

    @Autowired
    private AtaiCompetitionService ataiCompetitionService;

    @Autowired
    private AtaiUserCompetitionService ataiUserCompetitionService;

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private RedisTemplate redisTemplate;


    //1.根据比赛id，token(用户id)查询信息
    @ApiOperation (value = "根据比赛id，用户id查询信息")
    @GetMapping ("getUserCompetition/{competitionId}")
    public R getUserCompetition(@PathVariable String competitionId,
                                HttpServletRequest request) {
        JwtInfo jwtToken = null;
        try {
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        } catch (ExpiredJwtException e) {
            return R.error().code(28004).message("登录已过期，请重新登录");
        }
        if (StringUtils.isEmpty(jwtToken)) {
            return R.success();
        }
        String userId = jwtToken.getId();
        AtaiUserCompetition ataiUserCompetition = ataiUserCompetitionService.getByUseridCompetitionId(userId, competitionId);
        return R.success().data("userCompetition", ataiUserCompetition);
    }

    //2.根据比赛id，团队id查询信息
    @ApiOperation (value = "根据比赛id，团队id查询信息")
    @GetMapping ("getTeamCompetition/{competitionId}/{teamId}")
    public R getTeamCompetition(@PathVariable String competitionId, @PathVariable String teamId) {
        //1.查信息userId,teamName,score,deadline
        TeamCompetition[] teamCompetition = ataiUserCompetitionService.getTeamCompetition(competitionId, teamId);
        UcenterMemberOrder[] tmp = new UcenterMemberOrder[teamCompetition.length];//用户信息
        int index = 0;
        //2.查队友列表
        for (TeamCompetition a : teamCompetition) {
            //通过远程调用根据用户id获取用户信息
            UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(a.getUserId());
            tmp[index] = userInfoOrder;
            //查询用户信息
            index += 1;
        }
        teamCompetition[0].setFriend(tmp);
        return R.success().data("teamCompetition", teamCompetition[0]);
    }

    //3.注册：根据比赛id，用户id插入团队id，团队名称
    @ApiOperation (value = "插入团队id，团队名称")
    @GetMapping ("insertUserCompetition/{competitionId}")
    public R insertUserCompetition(@PathVariable String competitionId, HttpServletRequest request) {
        JwtInfo jwtToken = null;
        try {
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        } catch (ExpiredJwtException e) {
            return R.error().code(28004).message("登录已过期，请重新登录");
        }
        if (StringUtils.isEmpty(jwtToken)) {
            return R.error().code(28004).message("登录已过期，请重新登录");
        }

        AtaiCompetition ataiCompetition = ataiCompetitionService.getById(competitionId);

        //添加参与者
        ataiCompetition.setParticipants(ataiCompetition.getParticipants() + 1);
        ataiCompetitionService.updateById(ataiCompetition);
        Integer submitCounts = ataiCompetition.getSubmitCounts();

        AtaiUserCompetition ataiUserCompetition = new AtaiUserCompetition();
        String userId = jwtToken.getId();
        String teamName = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String teamId = String.valueOf(teamName.hashCode());
        ataiUserCompetition
                .setCompetitionId(competitionId)
                .setUserId(userId)
                .setTeamName(teamName)
                .setTeamId(teamId)
                .setSubmitCounts(submitCounts);
        boolean flag = ataiUserCompetitionService.save(ataiUserCompetition);
        if (flag) {
            return R.success().data("teamId", teamId);
        } else {
            return R.error();
        }
    }

    //4.根据比赛id，用户id删除记录
    @ApiOperation (value = "根据比赛id，用户id删除记录")
    @PostMapping ("deleteUserCompetition")
    public R deleteUserCompetition(@RequestBody AtaiUserCompetition ataiUserCompetition) {
        String userId = ataiUserCompetition.getUserId();
        String competitionId = ataiUserCompetition.getCompetitionId();
        boolean flag = ataiUserCompetitionService.deleteByUseridCompetitionId(userId, competitionId);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //5.更新分数，最优日期提交日,获取上传过来文件，把文件内容读取出来,算分
    @ApiOperation (value = "更新分数，最优日期提交日")
    @PostMapping ("saveResult/{competitionId}/{userId}")
    public R saveResult(MultipartFile file,
                        @ApiParam (value = "包含比赛id", required = true)
                        @PathVariable String competitionId,
                        @ApiParam (value = "用户id", required = true)
                        @PathVariable String userId) {
        try {
            //根据上传的文件算出得分
            ataiUserCompetitionService.saveResult(file, ataiUserCompetitionService, userId, competitionId);
            return R.success().message("上传成功");
        } catch (Exception e) {
            return R.error().code(20001).message("上传失败");
        }

    }

    //6.根据比赛id，查询根据比赛id，token(用户id)查询信息
    @ApiOperation (value = "根据比赛id，查询所有的排名")
    @GetMapping ("getRanking/{competitionId}")
    public R getRanking(@PathVariable String competitionId) {
        List<RankingQuery> ranking = ataiUserCompetitionService.getRanking(competitionId);
        List<RankingQuery> ranking1 = new ArrayList<>();
        List<String> checkContains = new ArrayList<>();
        int count = 0;
        for (RankingQuery r : ranking) {
            if ("".equals(r.getNickName())) {
                count++;
                return R.success().data("ranking", count);
            }
            if (!checkContains.contains(r.getNickName())) {
                checkContains.add(r.getNickName());
                ranking1.add(r);
            }
        }
        return R.success().data("ranking", ranking1);
    }

    // 根据姓名获取排名
    @ApiOperation (value = "根据比赛id，团队名，查询该团队的排名")
    @GetMapping ("getRank/{competitionId}/{nickName}")
    public R getRank(@PathVariable String competitionId, @PathVariable String nickName) {
        List<RankingQuery> ranking = ataiUserCompetitionService.getRanking(competitionId);
        int count = 0;
        for (RankingQuery r : ranking) {
            count++;
            if (r.getNickName().equals(nickName)) {
                return R.success().data("rank", count);
            }
        }
        return R.success().data("rank", null);
    }


    //查询当前用户的比赛列表
    @ApiOperation (value = "查询当前用户的比赛列表")
    @GetMapping ("getMyCompetitionList")
    public R getMyCompetitionList(HttpServletRequest request) {
        JwtInfo jwtToken = null;
        try {
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        } catch (ExpiredJwtException e) {
            return R.error().code(28004).message("登录已过期，请重新登录");
        }
        if (StringUtils.isEmpty(jwtToken)) {
            return R.error().code(28004).message("登录已过期，请重新登录");
        }
        String userId = jwtToken.getId();
        List<MyCompetition> data = ataiUserCompetitionService.getMyCompetitionList(userId);
        return R.success().data("data", data);
    }

    //通过key查询团队
    @ApiOperation (value = "根据比赛id，获取包含key的团队名")
    @GetMapping ("searchTeams/{competitionId}/{key}")
    public R searchTeams(@PathVariable String competitionId, @PathVariable (required = false) String key) {
        Set<TeamName> teams = ataiUserCompetitionService.searchTeamsByKey(competitionId, key);
        return R.success().data("teams", teams);
    }

    //修改团队名称
    @ApiOperation (value = "根据比赛id，teamId，修改团队名称")
    @GetMapping ("changeTeamName/{competitionId}/{oldTeamId}/{newTeamName}")
    public R changeTeamName(@PathVariable String competitionId,
                            @PathVariable String oldTeamId,
                            @PathVariable String newTeamName) {
        if ("@Random@".equals(newTeamName)) {
            newTeamName = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        }
        Boolean flag = ataiUserCompetitionService.changeTeamName(competitionId, oldTeamId, newTeamName);
        if (flag) {
            return R.success().data("newTeamName", newTeamName);
        } else {
            return R.error().message("团队名重复");
        }
    }

    //生成随机团队名称
    @ApiOperation (value = "通过比赛id，用户id，生成随机团队名称")
    @GetMapping ("createTeamName/{competitionId}/{userId}")
    public R createTeamName(@PathVariable String competitionId,
                            @PathVariable String userId) {
        ataiUserCompetitionService.createTeamName(competitionId, userId);
        return R.success();
    }

    //申请加入团队
    @ApiOperation (value = "通过比赛id和我的用户id，申请加入团队名为teamName的团队")
    @GetMapping ("applyToJoinTeam/{competitionId}/{teamName}/{userId}")
    public R applyToJoinTeam(@PathVariable String competitionId,
                             @PathVariable String teamName,
                             @PathVariable String userId) {
        try {
            ataiUserCompetitionService.applyToJoinTeam(competitionId, teamName, userId);
            return R.success();
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

    //获取我收到的申请
    @ApiOperation (value = "通过比赛id，用户id，获取我收到的申请")
    @GetMapping ("getSenders/{competitionId}/{receiveId}")
    public R getSenders(@PathVariable String competitionId,
                        @PathVariable String receiveId) {
        List<AtaiApplyMsg> list = ataiUserCompetitionService.getSenders(competitionId, receiveId);
        return R.success().data("senders", list);
    }

    //接受成员加入团队
    @ApiOperation (value = "根据比赛id，用户id,接受加入团队")
    @GetMapping ("acceptMember/{competitionId}/{senderId}/{userId}/{newTeamName}")
    public R acceptMember(@PathVariable String competitionId,
                          @PathVariable String senderId,
                          @PathVariable String userId,
                          @PathVariable String newTeamName) {
        Boolean flag = ataiUserCompetitionService.acceptMember(competitionId, senderId, userId, newTeamName);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }


    //拒绝成员加入团队
    @ApiOperation (value = "根据比赛id，用户id,拒绝加入团队")
    @GetMapping ("refuseMember/{competitionId}/{senderId}")
    public R refuseMember(@PathVariable String competitionId,
                          @PathVariable String senderId) {
        ataiUserCompetitionService.refuseMember(senderId, competitionId);
        return R.success();
    }

    //退出团队
    @ApiOperation (value = "根据比赛id，用户id,拒绝加入团队")
    @GetMapping ("quitTeam/{competitionId}/{userId}")
    public R quitTeam(@PathVariable String competitionId,
                      @PathVariable String userId) {
        ataiUserCompetitionService.createTeamName(competitionId, userId);
        return R.success();
    }

    //获取我的申请
    @ApiOperation (value = "根据比赛id，申请人id,获取申请信息")
    @GetMapping ("getReceivers/{competitionId}/{senderId}")
    public R getReceivers(@PathVariable String competitionId,
                          @PathVariable String senderId) {
        List<TeamName> list = ataiUserCompetitionService.getReceivers(competitionId, senderId);
        return R.success().data("receivers", list);
    }
}

