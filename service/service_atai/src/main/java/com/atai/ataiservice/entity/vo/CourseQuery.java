package com.atai.ataiservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseQuery {

    @ApiModelProperty (value = "课程名称,模糊查询")
    private String name;

    @ApiModelProperty (value = "时间")
    private String timeStatus;

    @ApiModelProperty (value = "课程级别")
    private String level;

    @ApiModelProperty (value = "技术")
    private String tech;
}