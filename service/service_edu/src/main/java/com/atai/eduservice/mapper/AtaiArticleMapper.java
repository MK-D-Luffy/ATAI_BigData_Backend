package com.atai.eduservice.mapper;

import com.atai.eduservice.entity.AtaiArticle;
import com.atai.eduservice.entity.frontvo.ArticleContentFront;
import com.atai.eduservice.entity.frontvo.ArticleFrontVo;
import com.atai.eduservice.entity.vo.ArticleQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
public interface AtaiArticleMapper extends BaseMapper<AtaiArticle> {

    List<ArticleFrontVo> findAll();

    IPage<ArticleFrontVo> findAllArticleCondition(@Param("page")Page<ArticleFrontVo> page, @Param("articleQuery") ArticleQuery articleQuery);

    //访问数+1
    Boolean addArticleView(String id);

    //评论数+1
    Boolean addArticleComments(String id);

    //查询
    ArticleContentFront getArticleById(String id);

}
