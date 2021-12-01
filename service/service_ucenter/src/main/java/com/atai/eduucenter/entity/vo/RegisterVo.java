package com.atai.eduucenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author baiyun
 */
@Data
public class RegisterVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "昵称")
    private String nickname;

    @ApiModelProperty (value = "手机号")
    private String mobile;

    @ApiModelProperty (value = "邮箱")
    private String email;

    @ApiModelProperty (value = "密码")
    private String password;

    @ApiModelProperty (value = "验证码")
    private String code;

    @ApiModelProperty (value = "验证码类型")
    private String codeType;
}
