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
@TableName ("atai_team_user")
@ApiModel (value = "AtaiTeamUser对象", description = "")
public class AtaiTeamUser extends Model<AtaiTeamUser> {

    private static final long serialVersionUID = 1L;

    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "队伍id")
    private String teamId;

    @ApiModelProperty (value = "用户id")
    private String userId;

    @ApiModelProperty (value = "比赛id")
    private String competitionId;

    @ApiModelProperty (value = "队员名")
    private String name;

    @ApiModelProperty (value = "队员宣言")
    private String intro;

    @ApiModelProperty (value = "是否是队长")
    private Integer isLeader;

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
