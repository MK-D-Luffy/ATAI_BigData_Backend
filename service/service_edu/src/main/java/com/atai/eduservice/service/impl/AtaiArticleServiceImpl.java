package com.atai.eduservice.service.impl;

import com.atai.eduservice.entity.AtaiArticle;
import com.atai.eduservice.entity.frontvo.ArticleContentFront;
import com.atai.eduservice.entity.frontvo.ArticleFrontVo;
import com.atai.eduservice.entity.vo.ArticleQuery;
import com.atai.eduservice.mapper.AtaiArticleMapper;
import com.atai.eduservice.service.AtaiArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<ArticleFrontVo> findAll() {
        List<ArticleFrontVo>  articleFrontVos = baseMapper.findAll();
        return articleFrontVos;
    }

    @Override
    public IPage<ArticleFrontVo> findAllArticleCondition(Page<ArticleFrontVo> pageArticle, ArticleQuery wrapper) {
        IPage<ArticleFrontVo>  articleFrontVos = baseMapper.findAllArticleCondition(pageArticle,wrapper);
        return articleFrontVos;
    }

    //查询，访问数+1
    @Override
    public ArticleContentFront getArticleById(String id) {
        //访问数+1
        Boolean flag = baseMapper.addArticleView(id);
        //查询
        ArticleContentFront articleContentFront = baseMapper.getArticleById(id);
        return articleContentFront;
    }

    @Override
    public Boolean addArticleComments(String id) {
        return baseMapper.addArticleComments(id);
    }
}
