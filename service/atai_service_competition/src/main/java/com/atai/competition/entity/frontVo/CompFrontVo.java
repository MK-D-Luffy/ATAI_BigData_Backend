package com.atai.competition.entity.frontVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class CompFrontVo {
    @ApiModelProperty (value = "比赛名称")
    private String name;

    @ApiModelProperty (value = "比赛类型")
    private String level;

    @ApiModelProperty (value = "比赛是否截止")
    private Boolean active;

    @ApiModelProperty (value = "热度排序")
    private String hotSort;

    @ApiModelProperty (value = "最新时间排序")
    @JsonFormat (timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    private String gmtCreateSort;

    @ApiModelProperty (value = "奖金排序")
    private String priceSort;
}
