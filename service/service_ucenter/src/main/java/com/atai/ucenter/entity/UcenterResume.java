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
 *
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-17
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@TableName ("ucenter_resume")
@ApiModel (value = "UcenterResume对象", description = "")
public class UcenterResume extends Model<UcenterResume> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "用户id")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "姓名")
    private String name;

    @ApiModelProperty (value = "年龄")
    private Integer age;

    @ApiModelProperty (value = "1 男 0 女")
    private Integer sex;

    @ApiModelProperty (value = "联系方式")
    private String mobile;

    @ApiModelProperty (value = "邮箱")
    private String email;

    @ApiModelProperty (value = "毕业学校")
    private String school;

    @ApiModelProperty (value = "学历")
    private String education;

    @ApiModelProperty (value = "联系地址")
    private String address;

    @ApiModelProperty (value = "项目经历")
    private String experience;

    @ApiModelProperty (value = "荣誉奖项")
    private String reward;

    @ApiModelProperty (value = "荣誉奖项")
    private String skill;

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
