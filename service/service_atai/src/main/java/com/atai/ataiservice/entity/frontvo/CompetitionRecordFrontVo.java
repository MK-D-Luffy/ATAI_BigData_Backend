package com.atai.ataiservice.entity.frontvo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class CompetitionRecordFrontVo extends Model<CompetitionRecordFrontVo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "提交的文件名")
    private String filename;
    @ApiModelProperty (value = "提交的文件地址")
    private String submitUrl;

    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "比赛id")
    private String competitionId;

    @ApiModelProperty (value = "队伍id")
    private String teamId;

    @ApiModelProperty (value = "用户id")
    private String userId;

    @ApiModelProperty (value = "用户名")
    private String username;

    @ApiModelProperty (value = "得分")
    private Double score;

    @ApiModelProperty (value = "创建时间")
    private Date gmtCreate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
