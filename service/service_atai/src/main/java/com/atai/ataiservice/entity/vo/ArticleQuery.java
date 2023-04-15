package com.atai.ataiservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleQuery {
    @ApiModelProperty (value = "文章标题,模糊查询")
    private String title;

    @ApiModelProperty (value = "最新,最热")
    private String status;
}
