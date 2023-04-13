package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.entity.AtaiCourseClass;
import com.atai.ataiservice.mapper.AtaiCourseClassMapper;
import com.atai.ataiservice.service.AtaiCourseClassService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-08
 */
@Service
public class AtaiCourseClassServiceImpl extends ServiceImpl<AtaiCourseClassMapper, AtaiCourseClass> implements AtaiCourseClassService {

    @Override
    public List<AtaiCourseClass> getListByCourseId(String courseId) {
        QueryWrapper<AtaiCourseClass> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.orderByAsc("`order`");
        return baseMapper.selectList(wrapper);
    }
}
