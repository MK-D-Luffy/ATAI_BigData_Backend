package com.atai.oss.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atai.oss.entity.ReadData;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//创建读取excel监听器
public class ExcelListener extends AnalysisEventListener<ReadData> {

    List<ReadData> list = new ArrayList<>();
    private RedisTemplate redisTemplate;
    private String url;

    public ExcelListener(String url, RedisTemplate redisTemplate) {
        this.url = url;
        this.redisTemplate = redisTemplate;
    }

    //一行一行去读取excle内容
    @Override
    public void invoke(ReadData user, AnalysisContext analysisContext) {
        System.out.println("***" + user);
        list.add(user);
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息：" + headMap);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        BoundHashOperations hashKey = redisTemplate.boundHashOps(this.url);
        //遍历结果,第一次放入redis
        for (ReadData a : list) {
            hashKey.put(a.getSid(), a.getSname());
        }
        //判断分数
        List value = redisTemplate.boundHashOps(url).values();
        //入库

    }
}