package com.atai.ataiservice.service;

import com.atai.ataiservice.entity.AtaiCompetition;
import com.atai.ataiservice.entity.vo.CompetitionQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 * 比赛 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
public interface AtaiCompetitionService extends IService<AtaiCompetition> {
    //1   分页查询比赛的方法
    Map<String, Object> getCompetitionPageList(Page<AtaiCompetition> compPage, CompetitionQuery competitionQuery);

    void submit(MultipartFile file, String competitionId, String teamId, String userId);
}
