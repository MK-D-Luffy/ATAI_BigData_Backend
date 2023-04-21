package com.atai.ucenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author baiyun
 */
@Data
public class ChangePwdVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "用户id")
    private String id;

    @ApiModelProperty (value = "手机号")
    private String mobile;

    @ApiModelProperty (value = "邮箱")
    private String email;

    @ApiModelProperty (value = "密码")
    private String password;

    @ApiModelProperty (value = "验证码")
    private String code;
}
