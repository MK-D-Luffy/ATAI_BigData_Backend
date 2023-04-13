package com.atai.competition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhuweiming <1905470291@qq.com>
 * @since 2021/7/28 16:10
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@ApiModel (value = "AtaiApplyMsg对象", description = "申请信息")
public class AtaiApplyMsg {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "用户比赛id")
    @TableId (value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty (value = "比赛id")
    private String competitionId;

    @ApiModelProperty (value = "申请信息发送者")
    private String senderId;

    @ApiModelProperty (value = "申请信息接收者")
    private String receiveId;

    @ApiModelProperty (value = "申请信息发送者")
    private String senderName;
}
