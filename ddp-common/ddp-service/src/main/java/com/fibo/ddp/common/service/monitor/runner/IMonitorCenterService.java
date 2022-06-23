package com.fibo.ddp.common.service.monitor.runner;

import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;

import java.util.List;
import java.util.Map;

/**
 * 监控中心服务相关
 */
public interface IMonitorCenterService {
    /**
     * 查询存储方式编码
     * @return 编码
     */
    String getStorageType();

    /**
     * 组装决策流监控 业务信息 存入Mysql/Hbase
     * @param inputParam 全量入参变量池
     * @param engine 引擎信息
     * @param engineVersion 引擎版本信息
     * @param outMap 输出变量池
     * @param paramJson  参数map
     * @param resultId 结果集表id
     */
    void monitorDecisionFlow(Map<String, Object> inputParam, Engine engine, EngineVersion engineVersion, List<EngineNode> engineNodeList, Map<String, Object> outMap, Map<String, Object> paramJson, Integer resultId);

}
