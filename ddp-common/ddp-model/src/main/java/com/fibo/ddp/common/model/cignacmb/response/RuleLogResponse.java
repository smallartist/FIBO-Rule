package com.fibo.ddp.common.model.cignacmb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleLogResponse {
    /**
     * 执行次数
     */
    private Integer executeNum;
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
     * 业务类型名称
     */
    private String businessName;
    /**
     * 业务类型编码
     */
    private String businessCode;
    /**
     * 业务子类型名称
     */
    private String businessChildName;
    /**
     * 业务子类型编码
     */
    private String businessChildCode;
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
}
