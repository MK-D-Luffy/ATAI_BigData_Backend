package com.atai.eduservice.controller;


import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.atai.commonutils.util.JwtInfo;
import com.atai.commonutils.util.JwtUtils;
import com.atai.eduservice.client.UcenterClient;
import com.atai.eduservice.entity.AtaiComment;
import com.atai.eduservice.entity.EduComment;
import com.atai.eduservice.entity.frontvo.CommentFrontVo;
import com.atai.eduservice.service.AtaiArticleService;
import com.atai.eduservice.service.AtaiCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.atai.commonutils.result.R;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Api(description="评论相关")
@RestController
@RequestMapping("/eduservice/atai-comment")
@Slf4j
public class AtaiCommentController {

    @Autowired
    private AtaiCommentService ataiCommentService;

    @Autowired
    private AtaiArticleService ataiArticleService;

    @Autowired
    private UcenterClient ucenterClient;

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
//        Page<AtaiComment> pageParam = new Page<>(page, limit);
//        QueryWrapper<AtaiComment> wrapper = new QueryWrapper<>();

//        if(!StringUtils.isEmpty(articleId)){
//            //构建条件
//            wrapper.eq("article_id",articleId);
//        }

        List<CommentFrontVo> commentList = ataiCommentService.getCommentsByArticleId(articleId);
//        ataiCommentService.page(pageParam,wrapper);
//        List<AtaiComment> commentList = pageParam.getRecords();
//
        Map<String, Object> map = new HashMap<>();
//        map.put("items", commentList);
//        map.put("current", pageParam.getCurrent());
//        map.put("pages", pageParam.getPages());
//        map.put("size", pageParam.getSize());
//        map.put("total", pageParam.getTotal());
//        map.put("hasNext", pageParam.hasNext());
//        map.put("hasPrevious", pageParam.hasPrevious());
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
        UcenterMemberOrder ucenterInfo = ucenterClient.getUserInfoOrder(jwtToken.getId());
        ataiCommentService.save(comment);
        //添加评论后，文章头评论数+1
        ataiArticleService.addArticleComments(comment.getArticleId());
        return R.success();
    }
}

