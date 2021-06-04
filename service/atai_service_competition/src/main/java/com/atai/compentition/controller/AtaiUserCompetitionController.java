package com.atai.compentition.controller;


import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.atai.commonutils.result.R;
import com.atai.commonutils.util.JwtInfo;
import com.atai.commonutils.util.JwtUtils;
import com.atai.compentition.client.UcenterClient;
import com.atai.compentition.entity.AtaiCompetition;
import com.atai.compentition.entity.AtaiUserCompetition;
import com.atai.compentition.entity.excel.CompeletionResult;
import com.atai.compentition.entity.frontVo.MyCompentition;
import com.atai.compentition.entity.vo.RankingQuery;
import com.atai.compentition.entity.vo.TeamCompetition;
import com.atai.compentition.service.AtaiUserCompetitionService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户比赛 前端控制器
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-18
 */
@Api(description="用户比赛管理")
@RestController
@RequestMapping("/atitcompetition/atai-user-competition")
public class AtaiUserCompetitionController {

    @Autowired
    private AtaiUserCompetitionService ataiUserCompetitionService;

    @Autowired
    private UcenterClient ucenterClient;


    //1.根据比赛id，token(用户id)查询信息
    @ApiOperation(value = "根据比赛id，用户id查询信息")
    @GetMapping("getUserCompetition/{compentitionId}")
    public R getUserCompetition(@PathVariable String compentitionId, HttpServletRequest request){
        JwtInfo jwtToken = null;
        try{
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        }catch (ExpiredJwtException e){
            return R.error().code(28004).message("登录超时，重新登录");
        }
        if(StringUtils.isEmpty(jwtToken)) {
            return R.error().code(28004).message("请登录");
        }
        String userId=jwtToken.getId();
        AtaiUserCompetition ataiUserCompetition = ataiUserCompetitionService.getByUseridCompetitionId(userId,compentitionId);
        return R.success().data("userCompetition",ataiUserCompetition);
    }

    //2.根据比赛id，团队id查询信息
    @ApiOperation(value = "根据比赛id，团队id查询信息")
    @GetMapping("getUserCompetition/{compentitionId}/{teamId}")
    public R getTeamCompetition(@PathVariable String compentitionId, @PathVariable String teamId){
        //1.查信息userId,teamName,score,deadline
        TeamCompetition[] teamCompetition = ataiUserCompetitionService.getTeamCompetition(compentitionId,teamId);
        UcenterMemberOrder[] tmp = new UcenterMemberOrder[teamCompetition.length];//用户信息
        int index = 0;
        //2.查队友列表
        for(TeamCompetition a :teamCompetition){
            //通过远程调用根据用户id获取用户信息
            UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(a.getUserId());
            tmp[index]=userInfoOrder;
            //查询用户信息
            index+=1;
        }
        teamCompetition[0].setFriend(tmp);
        return R.success().data("teamCompetition",teamCompetition[0]);
    }

    //3.注册：根据比赛id，用户id插入团队id，团队名称
    @ApiOperation(value = "插入团队id，团队名称")
    @PostMapping("insertUserCompetition")
    public R insertUserCompetition(@RequestBody AtaiUserCompetition ataiUserCompetition,HttpServletRequest request) {
//        int id =  Integer.parseInt(ataiUserCompetitionService.getMax())+1;
        AtaiUserCompetition ataiUserCompetition1 = new AtaiUserCompetition();
        JwtInfo jwtToken = null;
        try{
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        }catch (ExpiredJwtException e){
            return R.error().code(28004).message("登录超时，重新登录");
        }
        if(StringUtils.isEmpty(jwtToken)) {
            return R.error().code(28004).message("登录超时，重新登录");
        }
        String userId=jwtToken.getId();
        String compentitionId = ataiUserCompetition.getCompentitionId();
        String team_name = ataiUserCompetition.getTeamName();
        String team_id = String.valueOf(team_name.hashCode());
        Date date = new Date(System.currentTimeMillis());
        ataiUserCompetition1.setUserId(userId).setCompentitionId(compentitionId)
                .setTeamId(team_id).setTeamName(team_name);
        boolean flag = ataiUserCompetitionService.save(ataiUserCompetition1);
//        boolean flag = ataiUserCompetitionService.insertByUseridCompetitionId(id+"",userId,compentitionId,team_name,team_id,date);
        if(flag) {
            return R.success().data("teamId",team_id);
        } else {
            return R.error();
        }
    }

    //4.根据比赛id，用户id删除记录
    @ApiOperation(value = "根据比赛id，用户id删除记录")
    @PostMapping("deleteUserCompetition")
    public R deleteUserCompetition(@RequestBody AtaiUserCompetition ataiUserCompetition) {
        String userId = ataiUserCompetition.getUserId();
        String compentitionId = ataiUserCompetition.getCompentitionId();
        boolean flag = ataiUserCompetitionService.deleteByUseridCompetitionId(userId,compentitionId);
        if(flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //5.更新分数，最优日期提交日,获取上传过来文件，把文件内容读取出来,算分
    @ApiOperation(value = "更新分数，最优日期提交日")
    @PostMapping("saveResult/{compentitionId}/{userId}")
    public R saveResult(MultipartFile file,
                        @ApiParam(value = "包含比赛id", required = true)
                        @PathVariable String compentitionId,
                        @ApiParam(value = "用户id", required = true)
                            @PathVariable String userId) {
        try {
            //根据上传的文件算出得分
            ataiUserCompetitionService.saveResult(file,ataiUserCompetitionService,userId,compentitionId);
            return R.success().message("上传成功");
        }catch (Exception e){
            return R.error().code(20001).message("上传失败");
        }

    }

    //6.根据比赛id，查询根据比赛id，token(用户id)查询信息
    @ApiOperation(value = "根据比赛id，查询排名信息")
    @GetMapping("getRanking/{compentitionId}")
    public R getRanking(@PathVariable String compentitionId){
        List<RankingQuery> ranking= ataiUserCompetitionService.getRanking(compentitionId);
        List<RankingQuery> ranking1= new ArrayList<>();
        List<String> contains = new ArrayList<>();
        for(RankingQuery r:ranking){
            if(!contains.contains(r.getTeamName())){
                contains.add(r.getTeamName());
                ranking1.add(r);
            }
        }
//        RankingQuery[] result ;
//        //处理，相同队伍取最大
//        //遍历一次取出最大的那个，第一个出现的记录，后续出现的在记录中的为小值，删去即可
//        for(RankingQuery rq:ranking){
//             rq
//        }
        return R.success().data("ranking",ranking1);
    }

    //查询当前用户的比赛列表
    @ApiOperation(value = "查询当前用户的比赛列表")
    @GetMapping("getMyCompetitionList")
    public R getMyCompetitionList(HttpServletRequest request) {
        JwtInfo jwtToken = null;
        try{
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        }catch (ExpiredJwtException e){
            return R.error().code(28004).message("登录超时，重新登录");
        }
        if(StringUtils.isEmpty(jwtToken)) {
            return R.error().code(28004).message("登录超时，重新登录");
        }
        String userId=jwtToken.getId();
//        String userId = "1346808853676687362";
        List<MyCompentition> data = ataiUserCompetitionService.getMyCompetitionList(userId);
            return R.success().data("data",data);
    }

}

