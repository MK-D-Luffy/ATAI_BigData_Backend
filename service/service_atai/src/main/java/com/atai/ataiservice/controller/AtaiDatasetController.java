package com.atai.ataiservice.controller;


import cn.hutool.core.date.DateUtil;
import com.atai.ataiservice.client.OssClient;
import com.atai.ataiservice.entity.AtaiDataset;
import com.atai.ataiservice.entity.frontvo.DatasetFrontVo;
import com.atai.ataiservice.entity.vo.DatasetQuery;
import com.atai.ataiservice.service.AtaiDatasetService;
import com.atai.commonutils.result.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baiyunRain
 * @since 2023-03-22
 */
@RestController
@RequestMapping ("/ataiservice/atai-dataset")
public class AtaiDatasetController {
    @Autowired
    private AtaiDatasetService ataiDatasetService;
    @Autowired
    private OssClient ossClient;


    //1 根据数据集id进行查询
    @ApiOperation (value = "根据数据集id进行查询")
    @GetMapping ("getDataset/{id}")
    public R getDataset(@PathVariable String id) {
        DatasetFrontVo datasetFrontVo = ataiDatasetService.getFrontVoById(id);
        return R.success().data("dataset", datasetFrontVo);
    }

    //2 根据用户id进行查询
    @ApiOperation (value = "根据用户id进行查询")
    @GetMapping ("getUserDataset/{userid}")
    public R getUserDataset(@PathVariable String userid) {
        List<AtaiDataset> ataiDatasets = ataiDatasetService.getListById(userid);
        return R.success().data("dataset", ataiDatasets);
    }


    //3 条件查询带分页的方法
    @ApiOperation (value = "分页查询带条件")
    @PostMapping ("pageDataset/{page}/{limit}")
    public R pageDataset(@PathVariable long page, @PathVariable long limit,
                         @RequestBody (required = false) DatasetQuery datasetQuery) {  //@RequestBody(required = false)参数值可以为空
        Page<AtaiDataset> compPage = new Page<>(page, limit);
        Map<String, Object> map = ataiDatasetService.getDatasetPageList(compPage, datasetQuery);
        //返回分页所有数据
        return R.success().data(map);
    }

    //4 修改数据集功能
    @ApiOperation (value = "数据集修改")
    @PostMapping ("updateDataset")
    public R updateDataset(@RequestBody AtaiDataset ataiDataset) {
        ataiDataset.setGmtModified(DateUtil.date());
        boolean flag = ataiDatasetService.updateById(ataiDataset);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //5 添加数据集接口的方法
    @ApiOperation (value = "添加数据集")
    @PostMapping ("addDataset")
    public R addDataset(@RequestBody AtaiDataset ataiDataset) {
        ataiDataset.setIsDeleted(0);
        boolean save = ataiDatasetService.insert(ataiDataset);
        if (save) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //6 逻辑删除数据集的方法
    @ApiOperation (value = "逻辑删除数据集")
    @DeleteMapping ("/{id}")
    public R removeDataset(@ApiParam (name = "id", value = "比赛ID", required = true)
                           @PathVariable String id) {
        AtaiDataset ataiDataset = ataiDatasetService.getById(id);
        String datasetUrl = ataiDataset.getUrl();
        if (datasetUrl != null) {
            // 将OSS服务器和Redis中的数据删除
            ossClient.removeFile(datasetUrl);
        }
        // 将Mysql中的删除
        Boolean flag = ataiDatasetService.removeById(id);

        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //=====================================================================
    //===============================其他===============================
    //=====================================================================

    @ApiOperation (value = "根据用户id查询我发布的数据集")
    @GetMapping ("getUserDatasets/{userId}")
    public R getUserDatasets(@PathVariable String userId) {
        List<AtaiDataset> userDatasets = ataiDatasetService.getUserDatasets(userId);
        return R.success().data("userDatasets", userDatasets);
    }

    @ApiOperation (value = "添加浏览量")
    @GetMapping ("addWatch/{id}")
    public R addWatch(@PathVariable String id) {
        boolean updateFlag = ataiDatasetService.addWatch(id);
        if (updateFlag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    @ApiOperation (value = "添加下载量")
    @GetMapping ("addDownload/{id}")
    public R addDownload(@PathVariable String id) {
        boolean updateFlag = ataiDatasetService.addDownload(id);
        if (updateFlag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    @ApiOperation (value = "根据用户id和课程id判断是否已报名")
    @GetMapping ("getHotDatasets")
    public R getHotDatasets() {
        List<AtaiDataset> hotDatasets = ataiDatasetService.getHotDatasets();
        return R.success().data("hotDatasets", hotDatasets);
    }
}

