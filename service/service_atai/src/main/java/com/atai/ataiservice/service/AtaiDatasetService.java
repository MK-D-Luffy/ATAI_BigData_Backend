package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiDataset;
import com.atai.ataiservice.entity.frontvo.DatasetFrontVo;
import com.atai.ataiservice.entity.vo.DatasetQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-03-22
 */
public interface AtaiDatasetService extends IService<AtaiDataset> {

    public boolean insert(AtaiDataset ataiDataset);

    List<AtaiDataset> getListById(String userid);

    //分页查询比赛的方法
    Map<String, Object> getDatasetPageList(Page<AtaiDataset> datasetPage, DatasetQuery datasetQuery);

    boolean addWatch(String id);

    boolean addDownload(String id);

    List<AtaiDataset> getHotDatasets();

    List<AtaiDataset> getUserDatasets(String userId);

    DatasetFrontVo getFrontVoById(String id);
}
