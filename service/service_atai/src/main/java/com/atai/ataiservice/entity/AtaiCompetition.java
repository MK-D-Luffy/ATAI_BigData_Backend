package com.atai.ataiservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 比赛
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-09
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@TableName ("atai_competition")
@ApiModel (value = "AtaiCompetition对象", description = "比赛")
public class AtaiCompetition extends Model<AtaiCompetition> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "比赛ID")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "比赛名称")
    private String name;

    @ApiModelProperty (value = "比赛简介")
    private String intro;

    @ApiModelProperty (value = "赛题描述")
    private String description;

    @ApiModelProperty (value = "官方赛 训练赛 个人赛")
    private String level;

    @ApiModelProperty (value = "数据挖掘 自然语言处理 计算机视觉 AI其他")
    private String tech;

    @ApiModelProperty (value = "参赛人数")
    private Long participants;

    @ApiModelProperty (value = "赛季截止时间")
    private Date deadline;

    @ApiModelProperty (value = "奖金")
    private BigDecimal money;

    @ApiModelProperty (value = "可提交次数")
    private Integer submitCounts;

    @ApiModelProperty (value = "提交文件的类型 1代码 0结果文件")
    private Integer submitType;

    @ApiModelProperty (value = "数据集路径")
    private String data;

    @ApiModelProperty (value = "结果集路径")
    private String result;

    @ApiModelProperty (value = "大型赛事的封面")
    private String cover;

    @ApiModelProperty (value = "是否为大型赛事")
    private Integer isLarge;

    @ApiModelProperty (value = "逻辑删除 1（true）已删除， 0（false）未删除")
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
