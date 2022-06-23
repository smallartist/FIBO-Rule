package com.fibo.ddp.common.model.enginex.marketing.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 营销引擎节点当天结果VO
 */
@Data
public class MarketingNodeDateResultRspVo implements Serializable {
    private static final long serialVersionUID = 1874380391698401062L;
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
}

