package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.entity.AtaiCourseUser;
import com.atai.ataiservice.mapper.AtaiCourseUserMapper;
import com.atai.ataiservice.service.AtaiCourseUserService;
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
 * @since 2023-04-09
 */
@Service
public class AtaiCourseUserServiceImpl extends ServiceImpl<AtaiCourseUserMapper, AtaiCourseUser> implements AtaiCourseUserService {
    @Override
    public List<AtaiCourseUser> getListByUserId(String userId) {
        QueryWrapper<AtaiCourseUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public AtaiCourseUser getCourseByUCId(String userId, String courseId) {
        QueryWrapper<AtaiCourseUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("course_id", courseId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public boolean removeByUCId(String userId, String courseId) {
        QueryWrapper<AtaiCourseUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("course_id", courseId);
        return baseMapper.delete(wrapper) == 1;
    }
}
