package com.atai.competition.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CompetitionQuery {
    @ApiModelProperty (value = "比赛名称,模糊查询")
    private String name;

    @ApiModelProperty (value = "状态")
    private int status;

    @ApiModelProperty (value = "类型")
    private String level;

    @ApiModelProperty (value = "技术")
    private String tech;

    @ApiModelProperty (value = "最新,最热")
    private String sort;

}
