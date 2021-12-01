package com.atai.eduservice.controller.front;

import com.atai.commonutils.result.R;
import com.atai.eduservice.entity.EduTeacher;
import com.atai.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api ("前台首页：热门成员；热门课程")
@RestController
@RequestMapping("/eduservice/indexfront")
//@CrossOrigin
public class IndexFrontController {


    @Autowired
    private EduTeacherService teacherService;

    //查询前4条名师
    @ApiOperation(value = "查询前4条成员")
    @GetMapping("indexHotTeacher")
    public R indexHotTeacher() {
        List<EduTeacher> teacherList = teacherService.selectHotTeacher();
        return R.success().data("teacherList",teacherList);
    }


}
