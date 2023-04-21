package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.client.UcenterClient;
import com.atai.ataiservice.entity.AtaiDataset;
import com.atai.ataiservice.entity.frontvo.DatasetFrontVo;
import com.atai.ataiservice.entity.vo.DatasetQuery;
import com.atai.ataiservice.mapper.AtaiDatasetMapper;
import com.atai.ataiservice.service.AtaiDatasetService;
import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-03-22
 */
@Service
public class AtaiDatasetServiceImpl extends ServiceImpl<AtaiDatasetMapper, AtaiDataset> implements AtaiDatasetService {

    @Autowired
    private AtaiDatasetService ataiDatasetService;
    @Autowired
    private UcenterClient ucenterClient;


    @Override
    public boolean insert(AtaiDataset ataiDataset) {
        return baseMapper.insert(ataiDataset) == 1;
    }

    @Override
    public List<AtaiDataset> getListById(String userid) {
        QueryWrapper<AtaiDataset> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userid);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getDatasetPageList(Page<AtaiDataset> pageDataset, DatasetQuery datasetQuery) {

        //构建条件
        QueryWrapper<AtaiDataset> wrapper = new QueryWrapper<>();

        // 多条件组合查询
        // mybatis学过 动态sql
        String name = datasetQuery.getName();
        String sort = datasetQuery.getSort();
        String category = datasetQuery.getCategory();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(sort)) {
            switch (sort) {
                case "浏览量从高到低":
                    wrapper.orderByDesc("watch");
                    break;
                case "下载量从高到低":
                    wrapper.orderByDesc("download");
                    break;
                default:
                    wrapper.orderByDesc("gmt_create");
                    break;
            }
        }
        if (!StringUtils.isEmpty(category)) {
            wrapper.eq("category", category);
        }

        //把分页数据封装到pageDataset对象里去
        baseMapper.selectPage(pageDataset, wrapper);

        List<AtaiDataset> records = pageDataset.getRecords();
        ArrayList<DatasetFrontVo> vos = new ArrayList<>();
        for (AtaiDataset dataset : records) {
            DatasetFrontVo vo = new DatasetFrontVo();
            BeanUtils.copyProperties(dataset, vo);
            UcenterMemberOrder user = ucenterClient.getUserInfoOrder(dataset.getUserId());
            if (user != null) {
                vo.setUsername(user.getNickname());
                vo.setAvatar(user.getAvatar());
            }
            vos.add(vo);
        }

        long current = pageDataset.getCurrent();
        long pages = pageDataset.getPages();
        long size = pageDataset.getSize();
        long total = pageDataset.getTotal();
        boolean hasNext = pageDataset.hasNext();//下一页
        boolean hasPrevious = pageDataset.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", vos);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }

    @Override
    public List<AtaiDataset> getUserDatasets(String userId) {
        QueryWrapper<AtaiDataset> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public DatasetFrontVo getFrontVoById(String id) {
        DatasetFrontVo vo = new DatasetFrontVo();
        AtaiDataset ataiDataset = baseMapper.selectById(id);
        BeanUtils.copyProperties(ataiDataset, vo);

        String userId = ataiDataset.getUserId();
        UcenterMemberOrder user = ucenterClient.getUserInfoOrder(userId);
        if (user != null) {
            vo.setUsername(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }
        return vo;
    }

    @Override
    public boolean addWatch(String id) {
        AtaiDataset ataiDataset = baseMapper.selectById(id);
        ataiDataset.setWatch(ataiDataset.getWatch() + 1);
        return baseMapper.updateById(ataiDataset) == 1;
    }

    @Override
    public boolean addDownload(String id) {
        AtaiDataset ataiDataset = baseMapper.selectById(id);
        ataiDataset.setDownload(ataiDataset.getDownload() + 1);
        return baseMapper.updateById(ataiDataset) == 1;
    }

    @Override
    public List<AtaiDataset> getHotDatasets() {
        QueryWrapper<AtaiDataset> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("download").orderByDesc("watch");
        wrapper.last("limit 5");
        return baseMapper.selectList(wrapper);
    }
}
