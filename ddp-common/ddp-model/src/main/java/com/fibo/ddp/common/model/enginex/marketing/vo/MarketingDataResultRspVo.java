package com.fibo.ddp.common.model.enginex.marketing.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 营销引擎数据详情响应VO
 */
@Data
public class MarketingDataResultRspVo implements Serializable {
    private static final long serialVersionUID = -221159747546752923L;
    /**
     * 引擎版本id
     */
    private Integer engineVersionId;
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

    List<MarketingNodeResultRspVo> nodeResultRspVoList;
}
