package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiComment;
import com.atai.ataiservice.entity.frontvo.CommentFrontVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
public interface AtaiCommentService extends IService<AtaiComment> {

    //根据文章id获取评论列表
    List<CommentFrontVo> getCommentsByArticleId(String articleId);


//    //删除评论
//    boolean deleteById(String commentId, String memberId);
//
//    //判断是否是该用户的评论
//    boolean isComment(String commentId, String memberId);
}
