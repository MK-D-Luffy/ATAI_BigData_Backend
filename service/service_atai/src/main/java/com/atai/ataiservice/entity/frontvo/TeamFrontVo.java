package com.atai.ataiservice.entity.frontvo;

import com.atai.ataiservice.entity.AtaiTeamUser;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
@ApiModel (value = "AtaiTeam对象", description = "")
public class TeamFrontVo extends Model<TeamFrontVo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "队伍id")
    private String id;

    @ApiModelProperty (value = "头像")
    private String avatar;

    @ApiModelProperty (value = "队伍名")
    private String name;

    @ApiModelProperty (value = "队伍宣言")
    private String intro;

    @ApiModelProperty (value = "队员信息")
    private List<AtaiTeamUser> users;

}
