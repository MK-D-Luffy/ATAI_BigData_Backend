package com.atai.ataiservice.entity;

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
 * @since 2023-04-08
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@TableName ("atai_course")
@ApiModel (value = "AtaiCourse对象", description = "")
public class AtaiCourse extends Model<AtaiCourse> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "课程id")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "课程名")
    private String cover;

    @ApiModelProperty (value = "课程名")
    @TableField (value = "`name`")
    private String name;

    @ApiModelProperty (value = "简介")
    private String intro;

    @ApiModelProperty (value = "入门 进阶 实战")
    @TableField (value = "`level`")
    private String level;

    @ApiModelProperty (value = "技术")
    private String tech;

    @ApiModelProperty (value = "报名人数")
    private Integer participants;

    @ApiModelProperty (value = "开课时间")
    @TableField (value = "`begin`")
    private Date begin;

    @ApiModelProperty (value = "结课时间")
    @TableField (value = "`end`")
    private Date end;

    @ApiModelProperty (value = "课程描述")
    private String description;

    @ApiModelProperty (value = "前置知识")
    private String bLearning;

    @ApiModelProperty (value = "课程目标")
    private String goals;

    @ApiModelProperty (value = "资源信息")
    private String resource;

    @ApiModelProperty (value = "参考资料")
    @TableField (value = "`references`")
    private String references;

    @ApiModelProperty (value = "逻辑删除 1已删除 0未删除")
    @TableLogic
    private Integer isDeleted;

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
