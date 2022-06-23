package com.fibo.ddp.common.service.monitor.runner.hbase;

import com.fibo.ddp.common.model.monitor.decisionflow.MonitorDecisionFlow;

/**
 * 决策流监控 monitor_decision_flow Hbase服务
 */
public interface IMonitorDecisionFlow {
    /**
     * 将MonitorDecisionFlow 写入Hbase
     * @param monitorDecisionFlow
     * @return
     */
    boolean putMonitorDecisionFlowToHbase(MonitorDecisionFlow monitorDecisionFlow);
}
