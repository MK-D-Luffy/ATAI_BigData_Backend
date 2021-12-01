package com.atai.eduservice.service.impl;

import com.atai.eduservice.entity.AtaiComment;
import com.atai.eduservice.entity.frontvo.CommentFrontVo;
import com.atai.eduservice.mapper.AtaiCommentMapper;
import com.atai.eduservice.service.AtaiCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Service
public class AtaiCommentServiceImpl extends ServiceImpl<AtaiCommentMapper, AtaiComment> implements AtaiCommentService {

    //存放迭代找出的所有子代的集合
    private List<CommentFrontVo> tempReplys = new ArrayList<>();

    /*
        查询评论
     */
    @Override
    public List<CommentFrontVo> getCommentsByArticleId(String articleId) {
        //查询出父节点
        List<CommentFrontVo> comments = baseMapper.findByParentIdNull(articleId);
        for(CommentFrontVo commentFrontVo:comments){
            String id = commentFrontVo.getId();
            List<CommentFrontVo> childComments = baseMapper.findByParentIdNotNull(id);
            //查询子评论
            combineChildren(childComments);
            commentFrontVo.setChildrens(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return comments;
    }

    /*
        查询子评论
     */
    private void combineChildren(List<CommentFrontVo> childComments) {
        //判断是否有一级子回复
        if(childComments.size() > 0){
            //循环找出子评论的id
            for(CommentFrontVo childComment : childComments){
                String parentNickname = childComment.getNickname();
                tempReplys.add(childComment);
                String childId = childComment.getId();
                //查询二级以及所有子集回复
                recursively(childId, parentNickname);
            }
        }
    }

    /*
        循环迭代找出子集回复
     */
    private void recursively(String childId, String parentNickname1) {
        //根据子一级评论的id找到子二级评论
        List<CommentFrontVo> replayComments = baseMapper.findByParentIdNotNull(childId);

        if(replayComments.size() > 0){
            for(CommentFrontVo replayComment : replayComments){
                String parentNickname = replayComment.getNickname();
                String replayId = replayComment.getId();
                tempReplys.add(replayComment);
                //循环迭代找出子集回复
                recursively(replayId,parentNickname);
            }
        }
    }
//    //删除评论
//    @Override
//    public boolean deleteById(String commentId, String memberId) {
//        QueryWrapper<CommentFrontVo> queryWrapper = new QueryWrapper<>();
//        queryWrapper
//                .eq("id", commentId)
//                .eq("member_id", memberId);
//        return this.remove(queryWrapper);
//    }
//
//    //判断是否是该用户的评论
//    @Override
//    public boolean isComment(String commentId, String memberId) {
//        QueryWrapper<CommentFrontVo> queryWrapper = new QueryWrapper<>();
//        queryWrapper
//                .eq("id", commentId)
//                .eq("member_id", memberId);
//        Integer count = baseMapper.selectCount(queryWrapper);
//        return count.intValue() > 0;
//    }
}
