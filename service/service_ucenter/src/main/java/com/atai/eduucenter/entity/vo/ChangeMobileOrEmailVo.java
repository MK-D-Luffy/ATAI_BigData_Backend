package com.atai.eduucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhuweiming <1905470291@qq.com>
 * @since 2021/8/28 21:21
 */
@Data
public class ChangeMobileOrEmailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mobile;

    private String email;

    private String code;

}
