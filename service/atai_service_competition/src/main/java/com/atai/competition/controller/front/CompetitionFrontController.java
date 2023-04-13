package com.atai.competition.controller.front;

import com.atai.commonutils.result.R;
import com.atai.competition.entity.AtaiCompetition;
import com.atai.competition.entity.vo.CompetitionQuery;
import com.atai.competition.service.AtaiCompetitionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api (description = "比赛首页功能")
@RestController
@RequestMapping ("/atitcompetition/compfront")
//@CrossOrigin
public class CompetitionFrontController {

    @Autowired
    private AtaiCompetitionService ataiCompetitionService;

    //1 分页查询比赛的方法
    @ApiOperation (value = "条件查询带分页查询比赛")
    @PostMapping ("getCompetitionPageList/{current}/{limit}")
    public R getCompetitionPageList(@PathVariable long current, @PathVariable long limit,
                                    @RequestBody (required = false) CompetitionQuery competitionQuery) {

        Page<AtaiCompetition> compPage = new Page<>(current, limit);
        Map<String, Object> map = ataiCompetitionService.getCompetitionPageList(compPage, competitionQuery);
        //返回分页所有数据
        return R.success().data(map);
    }

}