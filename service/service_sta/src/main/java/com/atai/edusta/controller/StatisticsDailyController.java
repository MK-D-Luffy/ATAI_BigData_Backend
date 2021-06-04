package com.atai.edusta.controller;


import com.atai.commonutils.result.R;
import com.atai.edusta.entity.ArticleSta;
import com.atai.edusta.entity.CompCnumAndPnum;
import com.atai.edusta.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网站统计日数据 前端控制器
 * @author ZengJinming
 * @since 2020-04-13
 */
@Api(description="统计")
@RestController
@RequestMapping("/edusta/statistics")
//@CrossOrigin
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //统计某一天的注册人数,生成统计数据
    @ApiOperation(value = "统计某一天的注册人数2021-04-21")
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day){
        statisticsDailyService.registerCount(day);
        return R.success();
    }

    //各类比赛参赛人数-赛事数
    @ApiOperation(value = "各类比赛参赛人数-赛事数")
    @GetMapping("compCnumAndPnum")
    public R compCnumAndPnum(){
        List<CompCnumAndPnum> map = statisticsDailyService.compCnumAndPnum();
        List<String> level = new ArrayList<>();
        List<Integer> cnum = new ArrayList<>();
        List<Integer> pnum = new ArrayList<>();
        for(CompCnumAndPnum c:map){
            level.add(c.getLevel());
            cnum.add(c.getCnum());
            pnum.add(c.getPnum());
        }
        return R.success().data("level",level).data("cnum",cnum).data("pnum",pnum);
    }

    //文章根据分类对点击量与评论数的统计
    @ApiOperation(value = "文章根据分类对点击量与评论数的统计")
    @GetMapping("articleSta")
    public R articleSta(){
        List<ArticleSta> map = statisticsDailyService.articleSta();
        List<String> category = new ArrayList<>();
        List<Integer> cnum = new ArrayList<>();
        List<Integer> viewCounts = new ArrayList<>();
        List<Integer> commentCounts = new ArrayList<>();
        for(ArticleSta c:map){
            category.add(c.getCategory());
            viewCounts.add(c.getViewCounts());
            cnum.add(c.getCnum());
            commentCounts.add(c.getCommentCounts());
        }
        return R.success().data("category",category).data("cnum",cnum)
                .data("viewCounts",viewCounts).data("commentCounts",commentCounts);
    }
    //图表显示 返回两部分数据：日期json数组、数量json数组
    @ApiOperation(value = "图表显示")
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,@PathVariable String begin,
                      @PathVariable String end){
        Map<String, Object> map = statisticsDailyService.getShowData(type,begin,end);
        return R.success().data(map);
    }

}

