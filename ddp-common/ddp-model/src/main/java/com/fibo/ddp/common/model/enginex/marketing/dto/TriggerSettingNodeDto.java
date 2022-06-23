package com.fibo.ddp.common.model.enginex.marketing.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 触发设置节点dto
 */
@Data
public class TriggerSettingNodeDto implements Serializable {
    private static final long serialVersionUID = 1025007759237888120L;

    /**
     * 节点id
     */
    private Long nodeId;
    /**
     * 触发时间单位（每天、每周、每月）
     */
    private String triggerUnit;
    /**
     * 触发时间，精确到时分
     */
    private String triggerTime;
    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;
    /**
     * 参与类型 1：参与一次，2：参与多次
     */
    private int joinType;
}
