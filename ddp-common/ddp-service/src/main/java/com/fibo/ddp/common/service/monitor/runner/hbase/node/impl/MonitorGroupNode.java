package com.fibo.ddp.common.service.monitor.runner.hbase.node.impl;

import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.service.monitor.runner.hbase.node.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MonitorGroupNode implements MonitorService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void createMonitorStrategy(MonitorNode monitorNode, Map<String, Object> outMap) {
        //策略层面分组先不需要记录
    }
}
