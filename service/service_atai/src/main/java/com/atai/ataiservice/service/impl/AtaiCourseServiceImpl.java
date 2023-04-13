package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.client.UcenterClient;
import com.atai.ataiservice.entity.AtaiCourse;
import com.atai.ataiservice.entity.vo.CourseQuery;
import com.atai.ataiservice.mapper.AtaiCourseMapper;
import com.atai.ataiservice.service.AtaiCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-08
 */
@Service
public class AtaiCourseServiceImpl extends ServiceImpl<AtaiCourseMapper, AtaiCourse> implements AtaiCourseService {


    @Autowired
    private AtaiCourseService ataiCourseService;

    @Autowired
    private UcenterClient ucenterClient;


    @Override
    public boolean insert(AtaiCourse ataiCourse) {
        return baseMapper.insert(ataiCourse) == 1;
    }

    @Override
    public List<AtaiCourse> getListById(String userid) {
        QueryWrapper<AtaiCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userid);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getCoursePageList(Page<AtaiCourse> pageCourse, CourseQuery courseQuery) {

        //构建条件
        QueryWrapper<AtaiCourse> wrapper = new QueryWrapper<>();

        // 多条件组合查询
        // mybatis学过 动态sql
        String name = courseQuery.getName();
        String status = courseQuery.getTimeStatus();
        String tech = courseQuery.getTech();
        String level = courseQuery.getLevel();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        Date date = new Date();
        if (!StringUtils.isEmpty(status)) {
            switch (status) {
                case "进行中":
                    wrapper.gt("begin", date);
                    wrapper.lt("end", date);
                    break;
                case "即将开始":
                    wrapper.lt("begin", date);
                    break;
                case "已经结束":
                    wrapper.gt("end", date);
                    break;
                default:
                    break;
            }
        }
        if (!StringUtils.isEmpty(tech)) {
            wrapper.eq("tech", tech);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }

        //把分页数据封装到pageCourse对象里去
        baseMapper.selectPage(pageCourse, wrapper);

        List<AtaiCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();
        long pages = pageCourse.getPages();
        long size = pageCourse.getSize();
        long total = pageCourse.getTotal();
        boolean hasNext = pageCourse.hasNext();//下一页
        boolean hasPrevious = pageCourse.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }
}
