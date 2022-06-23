package com.fibo.ddp.common.service.monitor.runner.mysql.node.impl;

import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;
import com.fibo.ddp.common.service.monitor.runner.mysql.node.MonitorMysqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 决策选项节点
 */
@Service
public class MonitorMysqlDecisionOptionsNode implements MonitorMysqlService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void createMonitorStrategy(TMonitorNode monitorNode, Map<String, Object> outMap) {
        //决策选项不需要记录
    }
}
