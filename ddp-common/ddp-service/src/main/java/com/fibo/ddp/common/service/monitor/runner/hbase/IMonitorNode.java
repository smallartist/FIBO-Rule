package com.fibo.ddp.common.service.monitor.runner.hbase;

import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;

public interface IMonitorNode {
    /**
     * 将MonitorDecisionFlow 写入Hbase
     * @param monitorNode
     * @return
     */
    boolean putMonitorNodeToHbase(MonitorNode monitorNode);
}
