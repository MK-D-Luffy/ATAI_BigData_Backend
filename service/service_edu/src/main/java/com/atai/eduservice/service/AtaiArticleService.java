package com.atai.eduservice.service;

import com.atai.eduservice.entity.AtaiArticle;
import com.atai.eduservice.entity.frontvo.ArticleContentFront;
import com.atai.eduservice.entity.frontvo.ArticleFrontVo;
import com.atai.eduservice.entity.vo.ArticleQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
public interface AtaiArticleService extends IService<AtaiArticle> {

    List<ArticleFrontVo> findAll();

    IPage<ArticleFrontVo> findAllArticleCondition(Page<ArticleFrontVo> pageArticle, ArticleQuery wrapper);

    ArticleContentFront getArticleById(String id);

    //评论数+1
    Boolean addArticleComments(String id);
}
