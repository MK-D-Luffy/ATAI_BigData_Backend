package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiArticle;
import com.atai.ataiservice.entity.frontvo.ArticleBodyFrontVo;
import com.atai.ataiservice.entity.vo.ArticleQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
public interface AtaiArticleService extends IService<AtaiArticle> {

    ArticleBodyFrontVo getArticleById(String id);

    //评论数+1
    Boolean addArticleComments(String id);

    Map<String, Object> getArticlePageList(Page<AtaiArticle> articlePage, ArticleQuery articleQuery);
}
