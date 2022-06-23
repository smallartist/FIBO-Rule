package com.fibo.ddp.common.service.monitor.runner.mysql.node.impl;

import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;
import com.fibo.ddp.common.service.monitor.runner.mysql.node.MonitorMysqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MonitorMysqlChildEngineNode implements MonitorMysqlService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void createMonitorStrategy(TMonitorNode monitorNode, Map<String, Object> outMap) {
        //不需要记录
    }
}
