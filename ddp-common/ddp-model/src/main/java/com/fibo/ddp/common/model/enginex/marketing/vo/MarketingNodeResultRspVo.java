package com.fibo.ddp.common.model.enginex.marketing.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 营销引擎节点结果VO
 */
@Data
public class MarketingNodeResultRspVo implements Serializable {
    private static final long serialVersionUID = -4870605020047702174L;
    /**
     * 节点id
     */
    private Integer nodeId;
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

    List<MarketingNodeDateResultRspVo> nodeDateResultRspVoList;
}

