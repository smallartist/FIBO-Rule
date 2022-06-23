package com.fibo.ddp.common.model.datax.datamanage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (FieldCallLog)实体类
 *
 * @author jgp
 * @since 2021-12-08 14:18:29
 */
@Data
@TableName("t_field_call_log")
public class FieldCallLog implements Serializable {
    private static final long serialVersionUID = -22326067411898498L;
    /**
    * 自增主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 指标id
    */
    private Long fieldId;
    /**
     * 指标类型
     */
    private String fieldType;
    /**
     * 数据源类型
     */
    private String sourceType;
    /**
     * 数据源或者接口源的id
     */
    private Long sourceId;
    /**
    * 入参
    */
    private String inputParam;
    /**
    * 获取到的指标值
    */
    private String fieldValue;
    /**
     * 组织id
     */
    @TableField(exist = false)
    private Long organId;
    /**
    * 消耗时长，单位：毫秒
    */
    private Long duration;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;

    public FieldCallLog(Long fieldId, String fieldType,String sourceType,Long sourceId, String inputParam, String fieldValue, Long duration, Long organId) {
        this.fieldId = fieldId;
        this.fieldType = fieldType;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.inputParam = inputParam;
        this.fieldValue = fieldValue;
        this.duration = duration;
        this.organId = organId;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

}