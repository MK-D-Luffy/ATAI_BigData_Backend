package com.atai.ataiservice.controller;


import com.atai.ataiservice.client.OssClient;
import com.atai.ataiservice.client.UcenterClient;
import com.atai.ataiservice.entity.*;
import com.atai.ataiservice.entity.vo.CompetitionQuery;
import com.atai.ataiservice.service.*;
import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.atai.commonutils.result.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 比赛 前端控制器
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
@RestController
@RequestMapping ("/ataiservice/atai-competition")
public class AtaiCompetitionController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AtaiCompetitionController.class);
    @Autowired
    private OssClient ossClient;
    @Autowired
    private UcenterClient ucenterClient;
    @Autowired
    private AtaiCompetitionService ataiCompetitionService;
    @Autowired
    private AtaiCompetitionTeamService ataiCompetitionTeamService;
    @Autowired
    private AtaiTeamUserService ataiTeamUserService;
    @Autowired
    private AtaiTeamJoinService ataiTeamJoinService;
    @Autowired
    private AtaiCompetitionRecordService ataiCompetitionRecordService;

    //1 分页查询比赛的方法
    @ApiOperation (value = "条件查询带分页查询比赛")
    @PostMapping ("pageCompetitionCondition/{current}/{limit}")
    public R getCompetitionPageList(@PathVariable long current, @PathVariable long limit,
                                    @RequestBody (required = false) CompetitionQuery competitionQuery) {

        Page<AtaiCompetition> compPage = new Page<>(current, limit);
        Map<String, Object> map = ataiCompetitionService.getCompetitionPageList(compPage, competitionQuery);
        //返回分页所有数据
        return R.success().data(map);
    }

    //2 根据比赛id进行查询
    @ApiOperation (value = "根据比赛id进行查询")
    @GetMapping ("getCompetition/{id}")
    public R getCompetition(@PathVariable String id) {
        AtaiCompetition ataiCompetition = ataiCompetitionService.getById(id);
        return R.success().data("competition", ataiCompetition);
    }

    //3 添加比赛接口的方法
    @ApiOperation (value = "添加比赛")
    @PostMapping ("addCompetition")
    public R addCompetition(@RequestBody AtaiCompetition ataiCompetition) {
        boolean save = ataiCompetitionService.save(ataiCompetition);
        if (save) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //4 比赛修改功能
    @ApiOperation (value = "比赛修改")
    @PostMapping ("updateCompetition")
    public R updateCompetition(@RequestBody AtaiCompetition ataiCompetition) {
        boolean flag = ataiCompetitionService.updateById(ataiCompetition);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //5 逻辑删除比赛的方法
    @ApiOperation (value = "逻辑删除比赛")
    @DeleteMapping ("/{id}")
    public R removeCompetition(@ApiParam (name = "id", value = "比赛ID", required = true)
                               @PathVariable String id) {
        AtaiCompetition ataiCompetition = ataiCompetitionService.getById(id);
        String resultUrl = ataiCompetition.getResult();
        if (resultUrl != null) {
            // 将OSS服务器和Redis中的数据删除
            ossClient.removeFile(resultUrl);
        }
        // 将Mysql中的删除
        Boolean flag = ataiCompetitionService.removeById(id);

        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //==============================================================================
    //=================================团队管理====================================
    //==============================================================================

    //用户报名比赛
    //1 在比赛中创建队伍,在队伍中创建成员
    @ApiOperation (value = "报名比赛")
    @PostMapping ("attendCompetition/{userId}")
    public R attendCompetition(@RequestBody AtaiCompetitionTeam competitionTeam,
                               @PathVariable String userId) {
        try {
            ataiCompetitionTeamService.save(competitionTeam);
            String teamId = competitionTeam.getId();
            String competitionId = competitionTeam.getCompetitionId();
            Date gmtCreate = competitionTeam.getGmtCreate();
            Date gmtModified = competitionTeam.getGmtModified();

            AtaiTeamUser teamUser = new AtaiTeamUser();
            teamUser.setTeamId(teamId);
            teamUser.setUserId(userId);

            UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(userId);
            String nickname = userInfoOrder.getNickname();
            teamUser.setName(nickname);

            teamUser.setCompetitionId(competitionId);
            teamUser.setIsLeader(1);
            teamUser.setGmtCreate(gmtCreate);
            teamUser.setGmtModified(gmtModified);
            ataiTeamUserService.save(teamUser);
            return R.success();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    //根据用户id,比赛id查询队伍信息
    @ApiOperation (value = "根据用户id,比赛id查询是否报名比赛信息")
    @GetMapping ("getTeamByUserCompetition/{userId}/{competitionId}")
    public R getUserCompetition(@PathVariable String userId,
                                @PathVariable String competitionId) {
        AtaiTeamUser teamUser = ataiTeamUserService.getTeamByCUId(competitionId, userId);
        return R.success().data("teamUser", teamUser);
    }

    //根据teamId获取当前比赛的队伍信息
    @ApiOperation (value = "获取当前比赛的队伍信息")
    @GetMapping ("getCompetitionTeam/{teamId}")
    public R getCompetitionTeam(@PathVariable String teamId) {
        AtaiCompetitionTeam ataiCompetitionTeam = ataiCompetitionTeamService.getById(teamId);
        return R.success().data("data", ataiCompetitionTeam);
    }

    //根据队伍id获取队友
    @ApiOperation (value = "根据队伍id获取队友")
    @GetMapping ("getTeamUsers/{teamId}")
    public R getTeamUsers(@PathVariable String teamId) {
        List<AtaiTeamUser> users = ataiTeamUserService.getUsersByTeamId(teamId);
        return R.success().data("users", users);
    }


    //分页查找所有队伍
    @ApiOperation (value = "条件查询带分页查询队伍")
    @PostMapping ("pageTeamCondition/{current}/{limit}")
    public R getTeamPageList(@PathVariable long current, @PathVariable long limit,
                             @RequestParam (required = false) String name) {
        Page<AtaiCompetitionTeam> teamPage = new Page<>(current, limit);
        Map<String, Object> map = ataiCompetitionTeamService.getTeamPageList(teamPage, name);
        //返回分页所有数据
        return R.success().data(map);
    }


    //【组队申请相关】
    //设置是否允许被查找

    //通过用户id,比赛id,队伍id申请组队
    @ApiOperation (value = "通过用户id,比赛id,队伍id申请组队")
    @PostMapping ("joinTeam")
    public R joinTeam(@RequestBody (required = false) AtaiTeamJoin ataiTeamJoin) {
        Boolean flag = ataiTeamJoinService.save(ataiTeamJoin);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }


    //通过teamId和userId同意组队,修改teamUser的teamId
    @ApiOperation (value = "同意组队申请")
    @GetMapping ("acceptJoinTeam/{userId}/{competitionId}/{teamId}")
    public R acceptJoinTeam(@PathVariable String userId,
                            @PathVariable String competitionId,
                            @PathVariable String teamId) {
        AtaiTeamUser teamUser = ataiTeamUserService.getTeamByCUId(competitionId, userId);
        teamUser.setTeamId(teamId);
        teamUser.setIsLeader(0);

        String id = teamUser.getId();
        QueryWrapper<AtaiTeamUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);

        ataiTeamUserService.update(teamUser, wrapper);
        Boolean flag = ataiTeamJoinService.deleteByUCTId(userId, competitionId, teamId);
//        ataiCompetitionTeamService.removeById(teamId);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //拒绝组队申请
    @ApiOperation (value = "拒绝组队申请")
    @GetMapping ("refuseJoinTeam/{userId}/{competitionId}/{teamId}")
    public R refuseJoinTeam(@PathVariable String userId,
                            @PathVariable String competitionId,
                            @PathVariable String teamId) {
        Boolean flag = ataiTeamJoinService.deleteByUCTId(userId, competitionId, teamId);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }


    //通过teamId和competitionId查询申请和我组队的人
    @ApiOperation (value = "")
    @GetMapping ("getJoinTeamUser/{competitionId}/{teamId}")
    public R getJoinTeamUser(@PathVariable String competitionId,
                             @PathVariable String teamId) {
        List<AtaiTeamUser> users = ataiTeamJoinService.getUsersByCTId(competitionId, teamId);
        return R.success().data("users", users);
    }

    //==============================================================================
    //=================================作品提交====================================
    //==============================================================================

    @ApiOperation (value = "更新分数，最优日期提交日")
    @PostMapping ("submit/{competitionId}/{teamId}/{userId}")
    public R saveResult(MultipartFile file,
                        @ApiParam (value = "比赛id", required = true)
                        @PathVariable String competitionId,
                        @ApiParam (value = "团队id", required = true)
                        @PathVariable String teamId,
                        @ApiParam (value = "用户id", required = true)
                        @PathVariable String userId) {
        try {
            //根据上传的文件算出得分
            ataiCompetitionService.submit(file, competitionId, teamId, userId);
            return R.success().message("提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().code(20001).message("提交失败");
        }
    }

    //通过用户id获取提交结果
    @ApiOperation (value = "通过用户id获取提交结果")
    @GetMapping ("getRecordByUserId/{competitionId}/{userId}")
    public R getRecordByUserId(@PathVariable String competitionId,
                               @PathVariable String userId) {
        List<AtaiCompetitionRecord> records = ataiCompetitionRecordService.getRecordByUserId(userId);
        return R.success().data("records", records);
    }

    //通过队伍id获取提交结果
    @ApiOperation (value = "通过队伍id获取提交结果")
    @GetMapping ("getRecordByTeamId/{competitionId}/{teamId}")
    public R getRecordByTeamId(@PathVariable String competitionId,
                               @PathVariable String teamId) {
        List<AtaiCompetitionRecord> records = ataiCompetitionRecordService.getRecordByTeamId(teamId);
        return R.success().data("records", records);
    }


    //根据比赛id，查询根据比赛id，token(用户id)查询信息
    @ApiOperation (value = "根据比赛id，查询所有的排名")
    @GetMapping ("getRankList/{competitionId}")
    public R getRankList(@PathVariable String competitionId) {
        List<AtaiCompetitionTeam> rankList = ataiCompetitionTeamService.getRankList(competitionId);
        return R.success().data("rankList", rankList);
    }

}

