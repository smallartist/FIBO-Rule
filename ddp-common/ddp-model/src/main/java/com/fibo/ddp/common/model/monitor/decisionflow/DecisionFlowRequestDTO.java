package com.fibo.ddp.common.model.monitor.decisionflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 决策流监控请求参数 数据抽象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionFlowRequestDTO {
    /**
     * hbase中 monitor_decision_flow 对应的RowKey
     * t_resultset 表中字段hbase_row_key
     */
    private String hbaseRowKey;
    /**
     * 监控Mysql版本实现 决策流执行结果 记录id
     */
    private String resultId;
    /**
     * 监控上一层级记录id
     */
    private String monitorParentId;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 策略id
     */
    private String strategyId;
}
