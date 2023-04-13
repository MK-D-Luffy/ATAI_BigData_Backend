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
 * @since 2023-04-09
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@TableName ("atai_competition_team")
@ApiModel (value = "AtaiCompetitionTeam对象", description = "")
public class AtaiCompetitionTeam extends Model<AtaiCompetitionTeam> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "队伍id")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "比赛id")
    private String competitionId;

    @ApiModelProperty (value = "队伍头像")
    private String avatar;

    @ApiModelProperty (value = "队伍名")
    private String name;

    @ApiModelProperty (value = "队伍宣言")
    private String intro;

    @ApiModelProperty (value = "是否允许加入 1 允许 0 不允许")
    private Integer isAllowed;

    @ApiModelProperty (value = "得分")
    private Double score;

    @ApiModelProperty (value = "最优成绩提交日")
    private Date bestTime;

    @ApiModelProperty (value = "提交次数")
    private Integer submitCounts;

    @ApiModelProperty (value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty (value = "创建时间")
    @TableField (fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty (value = "修改时间")
    @TableField (fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
