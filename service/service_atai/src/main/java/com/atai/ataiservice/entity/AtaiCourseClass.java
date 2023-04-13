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
 * @since 2023-04-08
 */
@Data
@EqualsAndHashCode (callSuper = false)
@Accessors (chain = true)
@TableName ("atai_course_class")
@ApiModel (value = "AtaiCourseClass对象", description = "")
public class AtaiCourseClass extends Model<AtaiCourseClass> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty (value = "课时id")
    @TableId (value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty (value = "课程id")
    private String courseId;

    @ApiModelProperty (value = "课时名称")
    @TableField (value = "`name`")
    private String name;

    @ApiModelProperty (value = "课时视频")
    private String video;

    @ApiModelProperty (value = "课时文件")
    private String pdf;

    @ApiModelProperty (value = "课时号")
    @TableField (value = "`order`")
    private int order;

    @ApiModelProperty (value = "逻辑删除")
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
