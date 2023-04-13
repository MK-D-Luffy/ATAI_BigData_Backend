package com.atai.ataiservice.entity.frontvo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 写文章
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ArticlePublish对象", description="文章")
public class ArticlePublish implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "文章体ID")
    private String bodyId;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "评论数")
    @TableField(fill = FieldFill.INSERT)
    private Integer commentCounts;

    @ApiModelProperty(value = "浏览数")
    @TableField(fill = FieldFill.INSERT)
    private Integer viewCounts;

    @ApiModelProperty(value = "权重")
    @TableField(fill = FieldFill.INSERT)
    private Integer weight;

    @ApiModelProperty(value = "标签列表")
    private String tag;

    @ApiModelProperty(value = "html内容")
    private String contentHtml;

    //作者
    @ApiModelProperty(value = "作者名称")
    private String nickname;

    @ApiModelProperty(value = "作者id")
    private String authorId;

    @ApiModelProperty(value = "作者头像")
    private String avatar;

    //文章体
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "内容id")
    private String contentId;

    @ApiModelProperty(value = "文章分类")
    private String category;

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
