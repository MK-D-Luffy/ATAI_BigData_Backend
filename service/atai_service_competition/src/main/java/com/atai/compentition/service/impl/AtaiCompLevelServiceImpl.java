package com.atai.compentition.service.impl;

import com.atai.compentition.entity.AtaiCompLevel;
import com.atai.compentition.mapper.AtaiCompLevelMapper;
import com.atai.compentition.service.AtaiCompLevelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 比赛类别 服务实现类
 * </p>
 *
 * @author linshengbin
 * @since 2021-02-18
 */
@Service
public class AtaiCompLevelServiceImpl extends ServiceImpl<AtaiCompLevelMapper, AtaiCompLevel> implements AtaiCompLevelService {

    @Override
    public List<AtaiCompLevel> getAllSubject() {
        QueryWrapper<AtaiCompLevel> wrapper1 = new QueryWrapper<>();
        List<AtaiCompLevel> SubjectList1 = baseMapper.selectList(wrapper1);
        return SubjectList1;
    }
}
