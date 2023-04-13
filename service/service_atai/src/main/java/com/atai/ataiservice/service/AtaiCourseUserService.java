package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiCourseUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
public interface AtaiCourseUserService extends IService<AtaiCourseUser> {
    List<AtaiCourseUser> getListByUserId(String userId);

    AtaiCourseUser getCourseByUCId(String userId, String courseId);

    boolean removeByUCId(String userId, String courseId);
}
