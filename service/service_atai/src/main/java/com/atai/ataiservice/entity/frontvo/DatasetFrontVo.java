package com.atai.ataiservice.entity.frontvo;

import com.baomidou.mybatisplus.annotation.IdType;
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
 *
 * </p>
 *
 * @author baiyunRain
 * @since 2023-03-22
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@ApiModel (value = "AtaiDataset对象", description = "")
public class DatasetFrontVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "数据集id")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "用户id")
    private String userId;

    @ApiModelProperty (value = "用户名")
    private String username;

    @ApiModelProperty (value = "用户头像地址")
    private String avatar;

    @ApiModelProperty (value = "数据集图片")
    private String image;

    @ApiModelProperty (value = "数据集名称")
    private String name;

    @ApiModelProperty (value = "数据集简介")
    private String intro;

    @ApiModelProperty (value = "数据集路径")
    private String dataset;

    @ApiModelProperty (value = "数据集类别")
    private String category;

    @ApiModelProperty (value = "浏览量")
    private int watch;

    @ApiModelProperty (value = "下载量")
    private int download;

    @ApiModelProperty (value = "创建时间")
    private Date gmtCreate;
}
