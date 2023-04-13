package com.atai.ataiservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-13
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@TableName ("atai_competition_record")
@ApiModel (value = "AtaiCompetitionRecord对象", description = "")
public class AtaiCompetitionRecord extends Model<AtaiCompetitionRecord> {

    private static final long serialVersionUID = 1L;

    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "比赛id")
    private String competitionId;

    @ApiModelProperty (value = "队伍id")
    private String teamId;

    @ApiModelProperty (value = "用户id")
    private String userId;

    @ApiModelProperty (value = "提交的文件")
    private String file;

    @ApiModelProperty (value = "得分")
    private Double score;

    @ApiModelProperty (value = "备注")
    private String comment;

    @ApiModelProperty (value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty (value = "创建时间")
    @TableField (fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty (value = "更新时间")
    @TableField (fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
