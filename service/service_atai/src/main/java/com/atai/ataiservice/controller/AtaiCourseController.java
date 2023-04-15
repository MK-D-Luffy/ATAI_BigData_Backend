package com.atai.ataiservice.controller;


import com.atai.ataiservice.client.OssClient;
import com.atai.ataiservice.entity.AtaiCourse;
import com.atai.ataiservice.entity.AtaiCourseClass;
import com.atai.ataiservice.entity.AtaiCourseUser;
import com.atai.ataiservice.entity.vo.CourseQuery;
import com.atai.ataiservice.service.AtaiCourseClassService;
import com.atai.ataiservice.service.AtaiCourseService;
import com.atai.ataiservice.service.AtaiCourseUserService;
import com.atai.commonutils.result.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-08
 */
@RestController
@RequestMapping ("/ataiservice/atai-course")
public class AtaiCourseController {

    @Autowired
    private AtaiCourseService ataiCourseService;
    @Autowired
    private AtaiCourseClassService ataiCourseClassService;
    @Autowired
    private AtaiCourseUserService ataiCourseUserService;
    @Autowired
    private OssClient ossClient;


    //1 根据课程id进行查询
    @ApiOperation (value = "根据数据集id进行查询")
    @GetMapping ("getCourse/{id}")
    public R getCourse(@PathVariable String id) {
        AtaiCourse ataiCourse = ataiCourseService.getById(id);
        return R.success().data("course", ataiCourse);
    }

    //2 条件查询带分页的方法
    @ApiOperation (value = "分页查询带条件")
    @PostMapping ("pageCourse/{page}/{limit}")
    public R pageCourse(@PathVariable long page, @PathVariable long limit,
                        @RequestBody (required = false) CourseQuery courseQuery) {  //@RequestBody(required = false)参数值可以为空
        Page<AtaiCourse> compPage = new Page<>(page, limit);
        Map<String, Object> map = ataiCourseService.getCoursePageList(compPage, courseQuery);
        //返回分页所有数据
        return R.success().data(map);
    }


    //3 添加课程的方法
    @ApiOperation (value = "添加课程")
    @PostMapping ("addCourse")
    public R addCourse(@RequestBody AtaiCourse ataiCourse) {
        boolean save = ataiCourseService.insert(ataiCourse);
        if (save) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //4 课程修改功能
    @ApiOperation (value = "比赛修改")
    @PostMapping ("updateCourse")
    public R updateCourse(@RequestBody AtaiCourse ataiCourse) {
        boolean flag = ataiCourseService.updateById(ataiCourse);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //5 逻辑删除课程的方法
    @ApiOperation (value = "逻辑删除课程")
    @DeleteMapping ("/course/{id}")
    public R removeCourse(@ApiParam (name = "id", value = "课程ID", required = true)
                          @PathVariable String id) {
        // 将Mysql中的删除
        Boolean flag = ataiCourseService.removeById(id);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //=====================================================================
    //==============================课时管理===============================
    //=====================================================================


    //1 根据课程id查询课时
    @ApiOperation (value = "根据课程id查询课时")
    @GetMapping ("getCourseClass/{courseId}")
    public R getCourseClass(@PathVariable String courseId) {
        List<AtaiCourseClass> list = ataiCourseClassService.getListByCourseId(courseId);
        return R.success().data("courseClassList", list);
    }

    //2 根据课时id获取课时信息
    @ApiOperation (value = "根据课程id查询课时")
    @GetMapping ("getClass/{classId}")
    public R getClass(@PathVariable String classId) {
        AtaiCourseClass aClass = ataiCourseClassService.getById(classId);
        return R.success().data("class", aClass);
    }

    //2 根据课程添加课时
    @ApiOperation (value = "根据课程添加课时")
    @PostMapping ("addCourseClass")
    public R addCourseClass(@RequestBody AtaiCourseClass ataiCourseClass) {
        boolean save = ataiCourseClassService.save(ataiCourseClass);
        if (save) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //3 修改课时信息
    @ApiOperation (value = "修改课时信息")
    @PostMapping ("updateCourseClass")
    public R updateCourseClass(@RequestBody AtaiCourseClass ataiCourseClass) {
        boolean flag = ataiCourseClassService.updateById(ataiCourseClass);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //4 逻辑删除课时的方法
    @ApiOperation (value = "逻辑删除课时的方法")
    @DeleteMapping ("/class/{id}")
    public R removeCourseClass(@ApiParam (name = "id", value = "课时ID", required = true)
                               @PathVariable String id) {
        // 将Mysql中的删除
        Boolean flag = ataiCourseClassService.removeById(id);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //=====================================================================
    //===========================用户课程管理==============================
    //=====================================================================

    //1 查询用户报名的课程(以及学习情况)
    @ApiOperation (value = "查询用户报名的课程")
    @GetMapping ("getCourseUsers/{userId}")
    public R getCourseUsers(@PathVariable String userId) {
        List<AtaiCourse> list = ataiCourseService.getListByUserId(userId);
        return R.success().data("userCourses", list);
    }

    //2 根据用户id和课程id判断是否已报名
    @ApiOperation (value = "根据用户id和课程id判断是否已报名")
    @GetMapping ("getCourseUser/{userId}/{courseId}")
    public R getCourseUser(@PathVariable String userId,
                           @PathVariable String courseId) {
        AtaiCourseUser userCourse = ataiCourseUserService.getCourseByUCId(userId, courseId);
        return R.success().data("userCourse", userCourse);
    }

    //2 根据课程添加课时
    @ApiOperation (value = "根据课程添加课时")
    @PostMapping ("addCourseUser")
    public R addCourseUser(@RequestBody AtaiCourseUser ataiCourseUser) {
        boolean save = ataiCourseUserService.save(ataiCourseUser);
        if (save) {
            return R.success().data("userCourse", ataiCourseUser);
        } else {
            return R.error();
        }
    }

    //3 修改用户课程的信息(课时学习情况)
    @ApiOperation (value = "修改用户课程的信息")
    @PostMapping ("updateCourseUser")
    public R updateCourseUser(@RequestBody AtaiCourseUser ataiCourseUser) {
        boolean flag = ataiCourseUserService.updateById(ataiCourseUser);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //4 逻辑删除用户课程的方法
    @ApiOperation (value = "逻辑删除用户课程的方法")
    @DeleteMapping ("/user/{userId}/{courseId}")
    public R removeCourseUser(@PathVariable String userId, @PathVariable String courseId) {
        // 将Mysql中的删除
        Boolean flag = ataiCourseUserService.removeByUCId(userId, courseId);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //=====================================================================
    //===============================其他===============================
    //=====================================================================

    @ApiOperation (value = "根据用户id和课程id判断是否已报名")
    @GetMapping ("getHotCourses")
    public R getHotCourses() {
        List<AtaiCourse> hotCourses = ataiCourseService.getHotCourses();
        return R.success().data("hotCourses", hotCourses);
    }
}

