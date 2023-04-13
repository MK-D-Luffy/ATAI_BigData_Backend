package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiCourseClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-08
 */
public interface AtaiCourseClassService extends IService<AtaiCourseClass> {

    List<AtaiCourseClass> getListByCourseId(String courseId);

}
