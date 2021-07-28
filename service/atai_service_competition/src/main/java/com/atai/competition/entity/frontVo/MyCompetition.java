package com.atai.competition.entity.frontVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author baiyun
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@ApiModel (value = "MyCompetition对象", description = "我的比赛列表")
public class MyCompetition {
    @ApiModelProperty (value = "比赛ID")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "比赛名称")
    private String name;

    @ApiModelProperty (value = "比赛类型")
    private String level;

    @ApiModelProperty (value = "参赛人数")
    private Long participants;

    @ApiModelProperty(value = "赛季截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    @ApiModelProperty(value = "奖金")
    private BigDecimal money;

    @ApiModelProperty(value = "团队id")
    private String teamId;

    @ApiModelProperty(value = "分数")
    private Integer score;

    @ApiModelProperty(value = "团队名")
    private String teamName;
}
