package com.atai.ataiservice.service.impl;

import com.atai.ataiservice.client.OssClient;
import com.atai.ataiservice.entity.AtaiCompetition;
import com.atai.ataiservice.entity.AtaiCompetitionRecord;
import com.atai.ataiservice.entity.vo.CompetitionQuery;
import com.atai.ataiservice.mapper.AtaiCompetitionMapper;
import com.atai.ataiservice.service.AtaiCompetitionService;
import com.atai.commonutils.result.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 比赛 服务实现类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
@Service
public class AtaiCompetitionServiceImpl extends ServiceImpl<AtaiCompetitionMapper, AtaiCompetition> implements AtaiCompetitionService {

    @Autowired
    private OssClient ossClient;

    //1   分页查询比赛的方法
    @Override
    public Map<String, Object> getCompetitionPageList(Page<AtaiCompetition> pageComp, CompetitionQuery competitionQuery) {
        QueryWrapper<AtaiCompetition> wrapper = new QueryWrapper<>();

        //判断条件值是否为空，不为空拼接
        if (!StringUtils.isEmpty((competitionQuery.getName()))) {//关键字
            wrapper.like("name", competitionQuery.getName());
        }
        if (!StringUtils.isEmpty(competitionQuery.getLevel())) { //分类
            wrapper.eq("level", competitionQuery.getLevel());
        }
        if (!StringUtils.isEmpty(competitionQuery.getTech())) { //分类
            wrapper.eq("tech", competitionQuery.getTech());
        }

        Date date = new Date();
        if (competitionQuery.getStatus() == 1) {
            wrapper.gt("deadline", date);
        } else if (competitionQuery.getStatus() == 2) {
            wrapper.lt("deadline", date);
        }

        if ("最新".equals(competitionQuery.getSort())) {
            wrapper.orderByAsc("gmt_create");
        } else if ("最热".equals(competitionQuery.getSort())) {
            wrapper.orderByDesc("participants");
        }

        //把分页数据封装到pageComp对象里去
        baseMapper.selectPage(pageComp, wrapper);

        List<AtaiCompetition> records = pageComp.getRecords();
        long current = pageComp.getCurrent();
        long pages = pageComp.getPages();
        long size = pageComp.getSize();
        long total = pageComp.getTotal();
        boolean hasNext = pageComp.hasNext();//下一页
        boolean hasPrevious = pageComp.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
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
    public void submit(MultipartFile file, String competitionId, String teamId, String userId) {

        AtaiCompetitionRecord record = new AtaiCompetitionRecord();
        R r = ossClient.uploadOssFile(file);
        String url = (String) r.getData().get("url");
        System.out.println("r = " + r);
        System.out.println("url = " + url);

        //1 上传提交的文件

        //2 获取结果文件
        //3 计算得分
        //4 保存record对象,更新competitionTeam对象
    }

}
