package com.atai.compentition.entity.vo;

import com.atai.commonutils.ordervo.UcenterMemberOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeamCompetition {
    @ApiModelProperty(value = "团队名称")
    private String teamName;

    @ApiModelProperty(value = "队友")
    private String userId;

    @ApiModelProperty(value = "分数")
    private Integer score;

    @ApiModelProperty(value = "最优日期提交日")
    private Date deadline;

    @ApiModelProperty(value = "队友信息")
    private UcenterMemberOrder[] friend;
}
