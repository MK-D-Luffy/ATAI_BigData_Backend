package com.atai.competition.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhuweiming <1905470291@qq.com>
 * @since 2021/7/27 12:56
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@ApiModel (value = "TeamName对象", description = "团队名的展示")
public class TeamName {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "团队名")
    String teamName;
}
