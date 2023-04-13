package com.atai.ataiservice.entity.frontvo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文章内容
 * </p>
 *
 * @author linshengbin
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ArticleContentFront对象", description="文章")
public class ArticleContentFront implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "评论数")
    private Integer commentCounts;

    @ApiModelProperty(value = "浏览数")
    private Integer viewCounts;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "标签列表")
    private String tag;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

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


}
