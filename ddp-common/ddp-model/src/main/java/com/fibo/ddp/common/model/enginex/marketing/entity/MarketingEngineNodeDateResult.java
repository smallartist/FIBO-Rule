package com.fibo.ddp.common.model.enginex.marketing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 营销引擎节点当天结果表(MarketingEngineNodeDateResult)实体类
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_marketing_engine_node_date_result")
public class MarketingEngineNodeDateResult implements Serializable {
    private static final long serialVersionUID = -26970339169147139L;
    
    @TableId(type = IdType.AUTO)
    /**
     * 主键
     */
    private Integer id;
    /**
     * 引擎id
     */
    private Integer engineId;
    /**
     * 引擎版本id
     */
    private Integer engineVersionId;
    /**
     * 引擎名称
     */
    private String engineName;
    /**
     * 节点id
     */
    private Integer nodeId;
    /**
     * 当前日期
     */
    private Date currentDate;
    /**
     * 累计进入数
     */
    private Integer enterNum;
    /**
     * 累计触发数
     */
    private Integer touchNum;
    /**
     * 累计目标完成数
     */
    private Integer completeNum;
    /**
     * 目标完成率
     */
    private Float completeRate;
    /**
     * 组织编号
     */
    private Integer organId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}

