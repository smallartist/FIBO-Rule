package com.fibo.ddp.common.model.cignacmb.request;

import com.fibo.ddp.common.model.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleLogParam extends BaseParam implements Serializable {
    private static final long serialVersionUID = 1302021068405380361L;
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
     * 业务子类型名称
     */
    private String businessChildName;
    /**
     * 规则执行开始时间
     */
    private Date startTime;
    /**
     * 规则执行结束时间
     */
    private Date endTime;
    /**
     * 组织id
     */
    private Long organId;
}
