package com.atai.edusta.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CompCnumAndPnum对象", description="各类比赛参赛人数-赛事数")
public class CompCnumAndPnum {

    @ApiModelProperty(value = "比赛数")
    private Integer cnum;

    @ApiModelProperty(value = "比赛类别")
    private String level;

    @ApiModelProperty(value = "参赛人数")
    private Integer pnum;
}
