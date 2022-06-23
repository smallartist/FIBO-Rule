package com.fibo.ddp.common.model.cignacmb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLogDetailResponse {
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 规则编号
     */
    private String ruleCode;
    /**
     * 规则类型
     */
    private String ruleType;
    /**
     * 规则描述
     */
    private String ruleDescription;
    /**
     * 执行开关 1：开启，0：关闭
     */
    private Integer executeSwitch;
    /**
     * 执行有效期，开始时间
     */
    private Date validStartTime;
    /**
     * 执行有效期，结束时间
     */
    private Date validEndTime;
    /**
     * 规则输出结果
     */
    private String ruleResult;
}
