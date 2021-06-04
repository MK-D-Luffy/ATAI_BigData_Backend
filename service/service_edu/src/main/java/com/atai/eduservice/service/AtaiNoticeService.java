package com.atai.eduservice.service;

import com.atai.eduservice.entity.AtaiNotice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 公告 服务类
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-22
 */
public interface AtaiNoticeService extends IService<AtaiNotice> {

    List<AtaiNotice> getLatestNotice();
}
