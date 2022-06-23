package com.fibo.ddp.common.service.monitor.runner.hbase.node;

import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;

import java.util.Map;

/**
 * 监控信息组装
 */
public interface MonitorService {

    /**
     * 策略层面监控信息组装入库
     * @param monitorNode
     * @param outMap
     */
    void createMonitorStrategy(MonitorNode monitorNode, Map<String, Object> outMap);
}
