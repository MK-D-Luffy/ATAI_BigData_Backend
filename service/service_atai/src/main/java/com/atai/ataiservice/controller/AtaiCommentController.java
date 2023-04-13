package com.atai.ataiservice.controller;


import com.atai.ataiservice.entity.AtaiComment;
import com.atai.ataiservice.entity.frontvo.CommentFrontVo;
import com.atai.ataiservice.service.AtaiArticleService;
import com.atai.ataiservice.service.AtaiCommentService;
import com.atai.commonutils.result.R;
import com.atai.commonutils.util.JwtInfo;
import com.atai.commonutils.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Api ("评论相关")
@RestController
@RequestMapping ("/ataiservice/atai-comment")
public class AtaiCommentController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AtaiCommentController.class);
    @Autowired
    private AtaiCommentService ataiCommentService;

    @Autowired
    private AtaiArticleService ataiArticleService;

    //根据文章id查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("commentList/{page}/{limit}/{articleId}")
    public R getCommentsByArticleId(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "articleId", value = "文章id", required = true)
            @PathVariable String articleId) {

        List<CommentFrontVo> commentList = ataiCommentService.getCommentsByArticleId(articleId);
        return R.success().data("data",commentList);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("saveComment")
    public R save(@RequestBody AtaiComment comment, HttpServletRequest request) {
        JwtInfo jwtToken = null;
        try{
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        }catch (Exception e){
            return R.error().code(28004).message("请登录");
        }
        if(StringUtils.isEmpty(jwtToken)) {
            return R.error().code(28004).message("请登录");
        }
        comment.setAuthorId(jwtToken.getId());
        ataiCommentService.save(comment);
        //添加评论后，文章头评论数+1
        ataiArticleService.addArticleComments(comment.getArticleId());
        return R.success();
    }
}

