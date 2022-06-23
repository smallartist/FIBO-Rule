package com.fibo.ddp.common.model.cignacmb;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则执行记录(BusinessRuleLog)实体类
 */
@Data
@TableName("t_business_rule_log")
public class BusinessRuleLog implements Serializable {
    private static final long serialVersionUID = -63243451024796542L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 批次号
     */
    private String batchNo;
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
     * 规则输出结果
     */
    private String ruleResult;
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
     * 企业编号
     */
    private Integer organId;
    /**
     * 创建时间
     */
    private Date createTime;
}

