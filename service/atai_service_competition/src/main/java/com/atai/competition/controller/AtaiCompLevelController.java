package com.atai.competition.controller;


import com.atai.commonutils.result.R;
import com.atai.competition.entity.AtaiCompLevel;
import com.atai.competition.service.AtaiCompLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 比赛类别 前端控制器
 * </p>
 *
 * @author linshengbin
 * @since 2021-02-18
 */
@Api(description="比赛分类管理")
@RestController
@RequestMapping("/atitcompetition/atai-comp-level")
public class AtaiCompLevelController {

    @Autowired
    private AtaiCompLevelService ataiCompLevelService;

    //1 查询比赛分类所有数据
    @ApiOperation(value = "所有比赛分类列表")
    @GetMapping("findAll")
    public R findAllCompLevel() {
        //调用service的方法实现查询所有的操作
        List<AtaiCompLevel> list = ataiCompLevelService.list(null);
        return R.success().data("items",list);
    }

    //2 逻辑删除比赛分类的方法
    @ApiOperation(value = "逻辑删除比赛分类·")
    @DeleteMapping("{id}")
    public R removeCompLevel(@ApiParam(name = "id", value = "比赛分类·ID", required = true)
                               @PathVariable String id){
        Boolean flag = ataiCompLevelService.removeById(id);
        if (flag){
            return R.success();
        }else {
            return R.error();
        }
    }

    //3 添加比赛分类接口的方法
    @ApiOperation(value = "添加比赛分类")
    @PostMapping("addCompLevel")
    public R addCompLevel(@RequestBody AtaiCompLevel ataiCompLevel) {
        boolean save = ataiCompLevelService.save(ataiCompLevel);
        if(save) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //4 比赛分类修改功能
    @ApiOperation(value = "比赛分类修改")
    @PostMapping("updateCompLevel")
    public R updateCompLevel(@RequestBody AtaiCompLevel ataiCompLevel) {
        boolean flag = ataiCompLevelService.updateById(ataiCompLevel);
        if(flag) {
            return R.success();
        } else {
            return R.error();
        }
    }
}

