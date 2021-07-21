package com.atai.eduucenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangeVo {
    @ApiModelProperty (value = "手机号")
//    private String mobile;
    private String email;

    @ApiModelProperty (value = "密码")
    private String password;

    @ApiModelProperty (value = "验证码")
    private String code;
}
