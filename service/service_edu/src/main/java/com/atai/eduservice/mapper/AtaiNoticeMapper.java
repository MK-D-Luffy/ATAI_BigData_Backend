package com.atai.eduservice.mapper;

import com.atai.eduservice.entity.AtaiNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 公告 Mapper 接口
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-22
 */
public interface AtaiNoticeMapper extends BaseMapper<AtaiNotice> {

    List<AtaiNotice> getLatestNotice();
}
