package com.atai.eduservice.controller;


import com.atai.commonutils.result.R;
import com.atai.commonutils.util.JwtInfo;
import com.atai.commonutils.util.JwtUtils;
import com.atai.eduservice.entity.AtaiArticle;
import com.atai.eduservice.entity.AtaiArticleBody;
import com.atai.eduservice.entity.frontvo.ArticleContentFront;
import com.atai.eduservice.entity.frontvo.ArticleFrontVo;
import com.atai.eduservice.entity.frontvo.ArticlePublish;
import com.atai.eduservice.entity.vo.ArticleQuery;
import com.atai.eduservice.service.AtaiArticleBodyService;
import com.atai.eduservice.service.AtaiArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Api(description="文章相关")
@RestController
@RequestMapping("/eduservice/atai-article")
public class AtaiArticleController {

    @Autowired
    private AtaiArticleService ataiArticleService;

    @Autowired
    private AtaiArticleBodyService ataiArticleBodyService;

    //1 分页条件查询文章列表
    //rest风格
    @ApiOperation(value = "分页条件查询文章列表")
    @PostMapping("pageArticleCondition/{current}/{limit}")
    public R findAllArticleCondition(@PathVariable long current, @PathVariable long limit,
                                     @RequestBody(required = false)  ArticleQuery articleQuery) {
        Page<ArticleFrontVo> pageArticle = new Page<>(current,limit);
        IPage<ArticleFrontVo> result = ataiArticleService.findAllArticleCondition(pageArticle,articleQuery);
        List<ArticleFrontVo> list = result.getRecords();
        boolean hasNext = result.getTotal()>current*limit?true:false;//下一页
        boolean hasPrevious = current>1?true:false;//上一页
        //调用service的方法实现查询所有的操作
        return R.success().data("total",result.getTotal())
                .data("records",list).data("current",result.getCurrent())
                .data("pages",result.getPages())
                .data("hasNext",hasNext)
                .data("hasPrevious",hasPrevious);

    }

    //2 文章id查询文章内容
    //rest风格
    @ApiOperation(value = "文章id查询文章内容")
    @GetMapping("getArticleById/{id}")
    public R getArticleById(@PathVariable String id) {
        ArticleContentFront articleContentFront = ataiArticleService.getArticleById(id);
        return R.success().data("data",articleContentFront);
    }

    //查询当前用户的文章列表
    @ApiOperation(value = "查询当前用户的比赛列表")
    @GetMapping("getMyArticleList/{current}/{limit}")
    public R getMyArticleList(@PathVariable long current, @PathVariable long limit,
                              HttpServletRequest request) {
        JwtInfo jwtToken = null;
        try{
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        }catch (ExpiredJwtException e){
            return R.error().code(28004).message("登录超时，重新登录");
        }
        if(StringUtils.isEmpty(jwtToken)) {
            return R.error().code(28004).message("登录超时，重新登录");
        }
        String userId=jwtToken.getId();

        //创建page对象
        Page<AtaiArticle> pageCompetition = new Page<>(current,limit);
        //构建条件
        QueryWrapper<AtaiArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("author_id",userId);
        //排序
        wrapper.orderByDesc("gmt_create");
        ataiArticleService.page(pageCompetition,wrapper);
        long total = pageCompetition.getTotal();//总记录数
        List<AtaiArticle> records = pageCompetition.getRecords(); //数据list集合
        long pages = pageCompetition.getPages();
        long size = pageCompetition.getSize();
        boolean hasNext = pageCompetition.hasNext();//下一页
        boolean hasPrevious = pageCompetition.hasPrevious();//上一页
        return R.success().data("total",total).data("records",records)
                .data("pages",pages).data("size",size)
                .data("hasNext",hasNext).data("hasPrevious",hasPrevious);
    }

    //2 逻辑删除公告
    @ApiOperation(value = "逻辑删除文章")
    @GetMapping("removeArticle/{id}")
    public R removeArticle(@ApiParam(name = "id",value = "文章id",required = true)
                          @PathVariable String id){
        Boolean flag = ataiArticleService.removeById(id);
        if(flag){
            return R.success();
        }else {
            return R.error();
        }
    }


    //添加文章接口的方法
    @ApiOperation(value = "添加或修改文章")
    @PostMapping("addArticle")
    public R addArticle(@RequestBody ArticlePublish articlePublish, HttpServletRequest request) {
        JwtInfo jwtToken = null;
        try{
            jwtToken = JwtUtils.getMemberIdByJwtToken(request);
        }catch (Exception e){
            return R.error().code(28004).message("请登录");
        }
        if(StringUtils.isEmpty(jwtToken)) {
            return R.error().code(28004).message("请登录");
        }
        AtaiArticle ataiArticle = new AtaiArticle();
        AtaiArticleBody ataiArticleBody = new AtaiArticleBody();
        //把articlePublish对象的值赋给bean
        BeanUtils.copyProperties(articlePublish,ataiArticle);
        BeanUtils.copyProperties(articlePublish,ataiArticleBody);
        if(null!= articlePublish.getId() && articlePublish.getId().length()>0){
            //更新
            boolean update = ataiArticleService.updateById(ataiArticle);
            ataiArticleBody.setId(articlePublish.getBodyId());
            boolean update1 = ataiArticleBodyService.updateById(ataiArticleBody);
            if(update&&update1) {
                return R.success().data("articleId",ataiArticle.getId());
            } else {
                return R.error();
            }

        }else{
            //新增
            boolean save1 = ataiArticleBodyService.save(ataiArticleBody);

            //设置body_id
            ataiArticle.setBodyId(ataiArticleBody.getId());
            //设置author_id
            ataiArticle.setAuthorId(jwtToken.getId());
            boolean save = ataiArticleService.save(ataiArticle);

            if(save1&&save) {
                return R.success().data("articleId",ataiArticle.getId());
            } else {
                return R.error();
            }
        }



    }
}

