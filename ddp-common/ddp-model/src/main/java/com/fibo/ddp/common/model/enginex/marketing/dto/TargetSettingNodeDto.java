package com.fibo.ddp.common.model.enginex.marketing.dto;

import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import lombok.Data;

import java.io.Serializable;

/**
 * 目标设置节点dto
 */
@Data
public class TargetSettingNodeDto implements Serializable {
    private static final long serialVersionUID = -2245329320399100660L;
    /**
     * 节点id
     */
    private Long nodeId; 
    /**
     * 首要目标
     */
    // 目标时间
    private int primaryTime;
    // 目标时间对应的单位（分钟、小时、天）
    private String primaryTimeUnit;
    // 目标对应的条件
    private BaseRule primaryRule;
    // 条件id
    private Long primaryRuleId;

    /**
     * 次要目标（选填）
     */
    // 目标时间
    private int secondaryTime;
    // 目标时间对应的单位（分钟、小时、天）
    private String secondaryTimeUnit;
    // 目标对应的条件
    private BaseRule secondaryRule;
    // 条件id
    private Long secondaryRuleId;
}
