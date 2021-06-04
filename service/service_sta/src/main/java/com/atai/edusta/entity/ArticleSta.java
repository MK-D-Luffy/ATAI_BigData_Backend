package com.atai.edusta.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="articleSta", description="文章根据分类对点击量与评论数的统计")
public class ArticleSta {

    @ApiModelProperty(value = "文章数")
    private Integer cnum;

    @ApiModelProperty(value = "文章类别")
    private String category;

    @ApiModelProperty(value = "阅读数")
    private Integer viewCounts;

    @ApiModelProperty(value = "评论数")
    private Integer commentCounts;
}
