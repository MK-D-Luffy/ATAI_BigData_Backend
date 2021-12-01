package com.atai.eduservice.controller;

import com.atai.commonutils.result.R;
import com.atai.eduservice.entity.EduTeacher;
import com.atai.eduservice.entity.vo.TeacherQuery;
import com.atai.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实验室成员管理部分
 *
 * @author ZengJinming
 * @time 2020-03-29
 */

@Api ("实验室成员管理")
@RestController
@RequestMapping ("/eduservice/edu-teacher")
//@CrossOrigin
public class EduTeacherController {
    //访问地址：http://localhost:8001/eduservice/edu-teacher/findAll
    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //1 查询实验室成员表所有数据
    //rest风格
    @ApiOperation (value = "所有实验室成员列表")
    @GetMapping ("findAll")
    public R findAllTeacher() {
        //调用service的方法实现查询所有的操作
        List<EduTeacher> list = teacherService.list(null);
        return R.success().data("items", list);
    }


    //2 逻辑删除实验室成员的方法
    @ApiOperation (value = "逻辑删除实验室成员")
    @DeleteMapping ("{id}")
    public R removeTeacher(@ApiParam (name = "id", value = "实验室成员ID", required = true)
                           @PathVariable String id) {
        Boolean flag = teacherService.removeById(id);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //3 分页查询实验室成员的方法
    //current 当前页
    //limit 每页记录数
    @ApiOperation (value = "分页查询")
    @GetMapping ("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit) {
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher, null);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合
        return R.success().data("total", total).data("records", records);
    }

    //4 条件查询带分页的方法
    @ApiOperation (value = "分页查询带条件")
    @PostMapping ("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody (required = false) TeacherQuery teacherQuery) {  //@RequestBody(required = false)参数值可以为空
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

        // 多条件组合查询
        // mybatis学过 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询分页
        teacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合
        return R.success().data("total", total).data("records", records);
    }

    //添加实验室成员接口的方法
    @ApiOperation (value = "添加实验室成员")
    @PostMapping ("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        if (save) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //根据实验室成员id进行查询
    @ApiOperation (value = "根据实验室成员id进行查询")
    @GetMapping ("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.success().data("teacher", eduTeacher);
    }

    //实验室成员修改功能
    @ApiOperation (value = "实验室成员修改")
    @PostMapping ("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean flag = teacherService.updateById(eduTeacher);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }
}

