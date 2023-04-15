package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiCourse;
import com.atai.ataiservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-08
 */
public interface AtaiCourseService extends IService<AtaiCourse> {
    public boolean insert(AtaiCourse ataiCourse);

    List<AtaiCourse> getListById(String userid);

    //分页查询比赛的方法
    Map<String, Object> getCoursePageList(Page<AtaiCourse> coursePage, CourseQuery courseQuery);

    List<AtaiCourse> getHotCourses();

    List<AtaiCourse> getListByUserId(String userId);
}
