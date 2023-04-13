package com.atai.ataiservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DatasetQuery {
    @ApiModelProperty (value = "数据集名称,模糊查询")
    private String name;

    @ApiModelProperty (value = "排序")
    private String sort;

    @ApiModelProperty (value = "种类")
    private String category;
}