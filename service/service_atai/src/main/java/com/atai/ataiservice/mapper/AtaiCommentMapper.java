package com.atai.ataiservice.mapper;

import com.atai.ataiservice.entity.AtaiComment;
import com.atai.ataiservice.entity.frontvo.CommentFrontVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
public interface AtaiCommentMapper extends BaseMapper<AtaiComment> {

//    查询父级评论
    List<CommentFrontVo>  findByParentIdNull(String articleId);

//    查询一级回复
    List<CommentFrontVo> findByParentIdNotNull(String id);

}
