package com.atai.compentition.service;

import com.atai.compentition.entity.AtaiCompLevel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 比赛类别 服务类
 * </p>
 *
 * @author linshengbin
 * @since 2021-02-18
 */
public interface AtaiCompLevelService extends IService<AtaiCompLevel> {

    List<AtaiCompLevel> getAllSubject();
}
