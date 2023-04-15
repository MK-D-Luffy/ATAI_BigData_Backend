package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.entity.AtaiArticle;
import com.atai.ataiservice.entity.frontvo.ArticleBodyFrontVo;
import com.atai.ataiservice.entity.vo.ArticleQuery;
import com.atai.ataiservice.mapper.AtaiArticleMapper;
import com.atai.ataiservice.service.AtaiArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Service
public class AtaiArticleServiceImpl extends ServiceImpl<AtaiArticleMapper, AtaiArticle> implements AtaiArticleService {


    //查询，访问数+1
    @Override
    public ArticleBodyFrontVo getArticleById(String id) {
        //访问数+1
        Boolean flag = baseMapper.addArticleView(id);
        //查询
        return baseMapper.getArticleById(id);
    }

    @Override
    public Boolean addArticleComments(String id) {
        return baseMapper.addArticleComments(id);
    }

    @Override
    public Map<String, Object> getArticlePageList(Page<AtaiArticle> articlePage, ArticleQuery articleQuery) {
        //构建条件
        QueryWrapper<AtaiArticle> wrapper = new QueryWrapper<>();

        // 多条件组合查询
        // mybatis学过 动态sql
        String title = articleQuery.getTitle();
        String status = articleQuery.getStatus();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("name", title);
        }
        if (!StringUtils.isEmpty(status)) {
            if ("最新".equals(status)) {
                wrapper.orderByDesc("gmt_create");
            } else if ("最热".equals(status)) {
                wrapper.orderByDesc("view_counts");
            }
        }

        //把分页数据封装到articlePage对象里去
        baseMapper.selectPage(articlePage, wrapper);

        List<AtaiArticle> records = articlePage.getRecords();
        long current = articlePage.getCurrent();
        long pages = articlePage.getPages();
        long size = articlePage.getSize();
        long total = articlePage.getTotal();
        boolean hasNext = articlePage.hasNext();//下一页
        boolean hasPrevious = articlePage.hasPrevious();//上一页

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
