package com.fibo.ddp.common.service.monitor.runner.mysql.node;

import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;

import java.util.Map;

/**
 * 监控信息组装
 */
public interface MonitorMysqlService {

    /**
     * 策略层面监控信息组装入库
     * @param monitorNode
     * @param outMap
     */
    void createMonitorStrategy(TMonitorNode monitorNode, Map<String, Object> outMap);
}
