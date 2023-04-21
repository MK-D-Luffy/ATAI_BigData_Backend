package com.atai.ucenter.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-17
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@TableName ("ucenter_basic")
@ApiModel (value = "UcenterBasic对象", description = "会员表")
public class UcenterBasic extends Model<UcenterBasic> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "用户id")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "手机号")
    private String mobile;

    @ApiModelProperty (value = "邮箱")
    private String email;

    @ApiModelProperty (value = "密码")
    private String password;

    @ApiModelProperty (value = "昵称")
    private String nickname;

    @ApiModelProperty (value = "用户头像")
    private String avatar;

    @ApiModelProperty (value = "是否禁用 1（true）已禁用，  0（false）未禁用")
    private Boolean isDisabled;

    @ApiModelProperty (value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty (value = "创建时间")
    @TableField (fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty (value = "更新时间")
    @TableField (fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
