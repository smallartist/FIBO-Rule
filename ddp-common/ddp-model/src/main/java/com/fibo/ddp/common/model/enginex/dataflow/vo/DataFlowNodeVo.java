package com.fibo.ddp.common.model.enginex.dataflow.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import lombok.Data;

@Data
public class DataFlowNodeVo {
    /**
     * 节点名
     */
    private String nodeName;
    /**
     *
     */
    private String nodeType;
    /**
     * 规则部分
     */
    private BaseRule baseRule;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long baseRuleId;
    /**
     * 时间值
     */
    private Long fixedValue;
    /**
     * 时间单位
     */
    private String timeUnit;
}
