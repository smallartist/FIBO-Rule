package com.fibo.ddp.common.model.enginex.marketing.dto;

import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import lombok.Data;

import java.io.Serializable;

/**
 * 受众用户节点dto
 */
@Data
public class AudienceNodeDto implements Serializable {
    private static final long serialVersionUID = 1546466126108481180L;
    /**
     * 节点id
     */
    private Long nodeId;
    /**
     * 受众用户需要满足的条件
     */
    private BaseRule baseRule;
    /**
     * 条件id
     */
    private Long baseRuleId;
}
