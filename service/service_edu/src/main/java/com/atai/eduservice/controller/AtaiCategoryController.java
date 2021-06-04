package com.atai.eduservice.controller;


import com.atai.commonutils.result.R;
import com.atai.eduservice.entity.AtaiCategory;
import com.atai.eduservice.entity.EduTeacher;
import com.atai.eduservice.service.AtaiCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
import java.util.List;

/**
 * <p>
 * 分类 前端控制器
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Api(description="文章分类相关")
@RestController
@RequestMapping("/eduservice/atai-category")
public class AtaiCategoryController {

    @Autowired
    private AtaiCategoryService ataiCategoryService;

    //1 查询所有文章分类
    //rest风格
    @ApiOperation(value = "所有文章分类")
    @GetMapping("findAll")
    public R findAllArticleCategory() {
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("type",1);
        wrapper.eq("is_deleted",0);
        //调用service的方法实现查询所有的操作
        List<AtaiCategory> list = ataiCategoryService.list(wrapper);
        return R.success().data("data",list);
    }
}

