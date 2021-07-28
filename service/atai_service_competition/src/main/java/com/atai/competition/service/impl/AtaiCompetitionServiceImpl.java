package com.atai.competition.service.impl;

import com.atai.competition.entity.AtaiCompetition;
import com.atai.competition.entity.frontVo.CompFrontVo;
import com.atai.competition.mapper.AtaiCompetitionMapper;
import com.atai.competition.service.AtaiCompetitionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 比赛 服务实现类
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-08
 */
@Service
public class AtaiCompetitionServiceImpl extends ServiceImpl<AtaiCompetitionMapper, AtaiCompetition> implements AtaiCompetitionService {

    @Override
    public Map<String, Object> getCompetitionPageList(Page<AtaiCompetition> pageComp, CompFrontVo compFrontVo) {
        QueryWrapper<AtaiCompetition> wrapper = new QueryWrapper<>();

        //判断条件值是否为空，不为空拼接
        if (!StringUtils.isEmpty(compFrontVo.getLevel())) { //分类
            wrapper.eq("level", compFrontVo.getLevel());
        }
        if (!StringUtils.isEmpty((compFrontVo.getName()))) {//关键字
            wrapper.like("name", compFrontVo.getName());
        }

        Date date = new Date();
        if (compFrontVo.getActive()) {
            wrapper.gt("deadline", date);
        }

        if (!StringUtils.isEmpty(compFrontVo.getHotSort())) { //关注度
            if ("1".equals(compFrontVo.getHotSort())) {
                wrapper.orderByDesc("participants");
            } else {
                wrapper.orderByAsc("participants");
            }
        }
        if (!StringUtils.isEmpty(compFrontVo.getGmtCreateSort())) { //最新
            if ("1".equals(compFrontVo.getGmtCreateSort())) {
                wrapper.orderByDesc("gmt_create");
            } else {
                wrapper.orderByAsc("gmt_create");
            }
        } else {
            //默认按发布时间降序
            wrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(compFrontVo.getPriceSort())) {//价格
            if ("1".equals(compFrontVo.getPriceSort())) {
                wrapper.orderByDesc("money");
            } else {
                wrapper.orderByAsc("money");
            }
        }

        //把分页数据封装到pageComp对象里去
        baseMapper.selectPage(pageComp, wrapper);

        List<AtaiCompetition> records = pageComp.getRecords();
        long current = pageComp.getCurrent();
        long pages = pageComp.getPages();
        long size = pageComp.getSize();
        long total = pageComp.getTotal();
        boolean hasNext = pageComp.hasNext();//下一页
        boolean hasPrevious = pageComp.hasPrevious();//上一页

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
