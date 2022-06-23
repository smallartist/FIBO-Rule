package com.fibo.ddp.common.service.monitor.runner;

import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;

import java.util.List;
import java.util.Map;

public interface MonitorCommonService {
    /**
     * 组装该节点监控信息部分数据
     * @param inputParam
     * @param engineNode
     * @param outMap
     * @param monitorNodeInfoList
     */
    public void buildMonitorNode(Map<String, Object> inputParam, EngineNode engineNode, Map<String, Object> outMap, List<MonitorNode> monitorNodeInfoList);
}
