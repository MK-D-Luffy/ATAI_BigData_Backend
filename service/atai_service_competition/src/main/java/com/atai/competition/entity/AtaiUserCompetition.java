package com.atai.competition.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户比赛
 * </p>
 *
 * @author linshengbin
 * @since 2021-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AtaiUserCompetition对象", description="用户比赛")
public class AtaiUserCompetition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户比赛id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "用户id")
    private String userId;

    @ApiModelProperty (value = "比赛id")
    private String competitionId;

    @ApiModelProperty (value = "团队id")
    private String teamId;

    @ApiModelProperty (value = "分数")
    private Double score;

    @ApiModelProperty (value = "最优日期提交日")
    @JsonFormat (timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    @ApiModelProperty (value = "团队名")
    private String teamName;

    @ApiModelProperty (value = "团队地位如:管理员和普通成员")
    private Integer teamLevel;

    @ApiModelProperty (value = "可提交次数")
    private Integer submitCounts;

    @ApiModelProperty (value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty (value = "创建时间")
    @TableField (fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty (value = "更新时间")
    @TableField (fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
