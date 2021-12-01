package com.atai.eduservice.controller.front;

import com.atai.commonutils.result.R;
import com.atai.eduservice.entity.EduTeacher;
import com.atai.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api ("实验室成员页功能")
@RestController
@RequestMapping("/eduservice/teacherfront")
//@CrossOrigin
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;


    //1 分页查询实验室成员的方法
    @ApiOperation(value = "分页查询实验室成员的方法")
    @PostMapping("getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(@PathVariable long page, @PathVariable long limit) {
        Page<EduTeacher> pageTeacher = new Page<>(page,limit);
        Map<String,Object> map = teacherService.getTeacherFrontList(pageTeacher);
        //返回分页所有数据
        return R.success().data(map);
    }

    //2 实验室成员详情的功能
    @ApiOperation(value = "实验室成员详情的功能")
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId) {
        //1 根据实验室成员id查询实验室成员基本信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);
        return R.success().data("teacher",eduTeacher);
    }
}












