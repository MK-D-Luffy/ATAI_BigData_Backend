package com.atai.eduservice.controller;


import com.atai.commonutils.result.R;
import com.atai.eduservice.entity.AtaiNotice;
import com.atai.eduservice.entity.vo.NoticeQuery;
import com.atai.eduservice.service.AtaiNoticeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 公告 前端控制器
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/eduservice/atai-notice")
public class AtaiNoticeController {

    @Autowired
    private AtaiNoticeService ataiNoticeService;

    //1 前台显示最新的5条公告

    @ApiOperation(value = "最新公告列表")
    @GetMapping("getLatestNotice")
    public R getLatestNotice(){
        List<AtaiNotice> list = ataiNoticeService.getLatestNotice();
        return R.success().data("items",list);
    }

    //2 逻辑删除公告
    @ApiOperation(value = "逻辑删除公告")
    @GetMapping("removeNotice/{id}")
    public R removeNotice(@ApiParam(name = "id",value = "公告id",required = true)
                          @PathVariable String id){
        Boolean flag = ataiNoticeService.removeById(id);
        if(flag){
            return R.success();
        }else {
            return R.error();
        }
    }

    //3 分页条件查询
    @ApiOperation(value = "分页查询带条件")
    @PostMapping("pageNoticeCondition/{current}/{limit}")
    public R pageNoticeCondition(@PathVariable long current, @PathVariable long limit,
                                 @RequestBody(required = false) NoticeQuery noticeQuery){
        //创建page对象
        Page<AtaiNotice> page = new Page<>(current,limit);
        //构建条件
        QueryWrapper<AtaiNotice> wrapper = new QueryWrapper<>();

        //多条件组合查询
        String title = noticeQuery.getTitle();
        String begin = noticeQuery.getBegin();
        String end = noticeQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(title)) {
            //构建条件
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create",end);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法实现条件查询
        ataiNoticeService.page(page,wrapper);
        long total = page.getTotal();
        List<AtaiNotice> records = page.getRecords();
        return R.success().data("total",total).data("records",records);
    }

    //4 添加公告
    @ApiOperation(value = "添加公告")
    @PostMapping("addNotice")
    public R addNotice(@RequestBody AtaiNotice ataiNotice){
        boolean save = ataiNoticeService.save(ataiNotice);
        if(save){
            return R.success();
        }else {
            return R.error();
        }
    }

    //5 根据id进行查询
    @ApiOperation(value = "根据id进行查询")
    @GetMapping("getNoticeById/{id}")
    public R getNoticeById(@PathVariable String id){
        AtaiNotice ataiNotice = ataiNoticeService.getById(id);
        return R.success().data("data",ataiNotice);
    }

    //6 修改公告
    @ApiOperation(value = "公告修改")
    @PostMapping("updateNotice")
    public R updateNotice(@RequestBody AtaiNotice ataiNotice){
        boolean flag = ataiNoticeService.updateById(ataiNotice);
        if(flag){
            return R.success();
        }else {
            return R.error();
        }
    }


}

