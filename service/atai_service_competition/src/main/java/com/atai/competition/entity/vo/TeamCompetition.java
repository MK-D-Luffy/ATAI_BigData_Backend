package com.atai.competition.entity.vo;

import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TeamCompetition {
    @ApiModelProperty (value = "团队名称")
    private String teamName;

    @ApiModelProperty (value = "我的用户id")
    private String userId;

    @ApiModelProperty (value = "分数")
    private Double score;

    @ApiModelProperty (value = "最优日期提交日")
    @JsonFormat (timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    @ApiModelProperty (value = "队友信息")
    private UcenterMemberOrder[] friend;
}
