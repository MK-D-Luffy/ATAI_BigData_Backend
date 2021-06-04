package com.atai.eduservice.service.impl;

import com.atai.eduservice.entity.AtaiNotice;
import com.atai.eduservice.mapper.AtaiNoticeMapper;
import com.atai.eduservice.service.AtaiNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 公告 服务实现类
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-22
 */
@Service
public class AtaiNoticeServiceImpl extends ServiceImpl<AtaiNoticeMapper, AtaiNotice> implements AtaiNoticeService {

    @Override
    public List<AtaiNotice> getLatestNotice() {
        return baseMapper.getLatestNotice();
    }
}
