package com.atai.eduservice.entity.frontvo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 评论列表对象
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CommentFrontVo对象", description="评论")
public class CommentFrontVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "内容")
    private String content;

    //作者
    @ApiModelProperty(value = "作者名称")
    private String nickname;

    @ApiModelProperty(value = "作者id")
    private String authorId;

    @ApiModelProperty(value = "作者头像")
    private String avatar;

    @ApiModelProperty(value = "文章ID")
    private String articleId;

    @ApiModelProperty(value = "父评论ID")
    private String parentId;

    @ApiModelProperty(value = "父作者ID")
    private String toUid;

    @ApiModelProperty(value = "所属层数")
    private String level;

    @ApiModelProperty(value = "@名称")
    private String toUsername;

    @ApiModelProperty(value = "子集列表")
    private List<CommentFrontVo> childrens;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
