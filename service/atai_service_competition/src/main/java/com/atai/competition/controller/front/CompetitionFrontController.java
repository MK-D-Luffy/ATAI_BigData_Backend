package com.atai.competition.controller.front;

import com.atai.commonutils.result.R;
import com.atai.competition.entity.AtaiCompLevel;
import com.atai.competition.entity.AtaiCompetition;
import com.atai.competition.entity.frontVo.CompFrontVo;
import com.atai.competition.service.AtaiCompLevelService;
import com.atai.competition.service.AtaiCompetitionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description="比赛首页功能")
@RestController
@RequestMapping("/atitcompetition/compfront")
//@CrossOrigin
public class CompetitionFrontController {

    @Autowired
    private AtaiCompetitionService ataiCompetitionService ;

    @Autowired
    private AtaiCompLevelService ataiCompLevelService;

    //1 分页查询比赛的方法
    @ApiOperation(value = "条件查询带分页查询比赛")
    @PostMapping("getCompetitionPageList/{page}/{limit}")
    public R getCompetitionPageList(@PathVariable long page, @PathVariable long limit,
                                    @RequestBody(required = false) CompFrontVo compFrontVo){

        Page<AtaiCompetition> compPage = new Page<>(page,limit);
        Map<String,Object> map = ataiCompetitionService.getCompetitionPageList(compPage,compFrontVo);
        //返回分页所有数据
        return R.success().data(map);
    }

    //2 查询比赛分类
    //比赛分类列表功能  树形结构显示
    @ApiOperation(value = "比赛分类列表")
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        //list集合泛型是一级分类  一级分类下包含二级分类
        List<AtaiCompLevel> list = ataiCompLevelService.getAllSubject();
        return R.success().data("list",list);
    }

}