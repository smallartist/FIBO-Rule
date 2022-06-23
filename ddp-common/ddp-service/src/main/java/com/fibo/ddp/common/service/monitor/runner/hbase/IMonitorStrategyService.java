package com.fibo.ddp.common.service.monitor.runner.hbase;

import com.fibo.ddp.common.model.monitor.decisionflow.MonitorStrategy;

public interface IMonitorStrategyService {
    /**
     * 将MonitorStrategy 写入Hbase
     * @param monitorStrategy
     * @return
     */
    boolean putMonitorStrategyToHbase(MonitorStrategy monitorStrategy);
}
