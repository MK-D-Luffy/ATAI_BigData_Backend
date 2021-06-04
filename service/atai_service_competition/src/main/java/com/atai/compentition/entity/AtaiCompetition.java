package com.atai.compentition.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 比赛
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AtaiCompetition对象", description="比赛")
public class AtaiCompetition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "比赛ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "比赛名称")
    private String name;

    @ApiModelProperty(value = "比赛简介")
    private String intro;

    @ApiModelProperty(value = "赛题描述")
    private String description;

    @ApiModelProperty(value = "算法大赛...")
    private String level;

    @ApiModelProperty(value = "参赛人数")
    private Long participants;

    @ApiModelProperty(value = "赛季截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;

    @ApiModelProperty(value = "奖金")
    private BigDecimal money;

    @ApiModelProperty(value = "赛题数据路径")
    private String cover;

    @ApiModelProperty(value = "结果集路径")
    private String result;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
