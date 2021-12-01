package com.atai.edusta.mapper;

import com.atai.edusta.entity.ArticleSta;
import com.atai.edusta.entity.CompCnumAndPnum;
import com.atai.edusta.entity.StatisticsDaily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 网站统计日数据 Mapper 接口
 * @author ZengJinming
 * @since 2020-04-13
 */
public interface StatisticsDailyMapper extends BaseMapper<StatisticsDaily> {

    List<CompCnumAndPnum> compCnumAndPnum();

    List<ArticleSta> articleSta();
}
