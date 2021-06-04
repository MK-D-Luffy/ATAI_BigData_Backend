package com.atai.eduservice.service.impl;

import com.atai.eduservice.mapper.EduCommentMapper;
import com.atai.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.atai.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 评论 服务实现类
 * @author ZengJinming
 * @since 2020-05-30
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    //删除评论
    @Override
    public boolean deleteById(String commentId, String memberId) {
        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", commentId)
                .eq("member_id", memberId);
        return this.remove(queryWrapper);
    }

    //判断是否是该用户的评论
    @Override
    public boolean isComment(String commentId, String memberId) {
        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", commentId)
                .eq("member_id", memberId);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count.intValue() > 0;
    }
}
