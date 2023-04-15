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
 * @since 2023-03-22
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@TableName ("atai_dataset")
@ApiModel (value = "AtaiDataset对象", description = "")
public class AtaiDataset extends Model<AtaiDataset> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "数据集id")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "用户id")
    private String userId;

    @ApiModelProperty (value = "数据集图片")
    private String image;

    @ApiModelProperty (value = "数据集名称")
    private String name;

    @ApiModelProperty (value = "数据集简介")
    private String intro;

    @ApiModelProperty (value = "数据集路径")
    private String url;

    @ApiModelProperty (value = "数据集类别")
    private String category;

    @ApiModelProperty (value = "浏览量")
    private int watch;

    @ApiModelProperty (value = "下载量")
    private int download;

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
