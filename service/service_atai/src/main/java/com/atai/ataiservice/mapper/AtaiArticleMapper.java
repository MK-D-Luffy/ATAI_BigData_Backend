package com.atai.ataiservice.mapper;

import com.atai.ataiservice.entity.AtaiArticle;
import com.atai.ataiservice.entity.frontvo.ArticleBodyFrontVo;
import com.atai.ataiservice.entity.frontvo.ArticleFrontVo;
import com.atai.ataiservice.entity.vo.ArticleQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
public interface AtaiArticleMapper extends BaseMapper<AtaiArticle> {


    IPage<ArticleFrontVo> findAllArticleCondition(@Param("page")Page<ArticleFrontVo> page, @Param("articleQuery") ArticleQuery articleQuery);

    //访问数+1
    Boolean addArticleView(String id);

    //评论数+1
    Boolean addArticleComments(String id);

    //查询
    ArticleBodyFrontVo getArticleById(String id);

}
