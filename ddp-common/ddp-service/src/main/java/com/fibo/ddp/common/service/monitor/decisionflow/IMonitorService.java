package com.fibo.ddp.common.service.monitor.decisionflow;

import com.fibo.ddp.common.model.enginex.risk.EngineResultSet;
import com.fibo.ddp.common.model.monitor.decisionflow.DecisionFlowRequestDTO;

import java.util.List;
import java.util.Map;

public interface IMonitorService {
    /**
     * 查询交通方式编码
     * @return 编码
     */
    String getStorageType();

    List<EngineResultSet> getEngineResultSetBySegment(Map map);

    /**
     * 根绝hbaseRowKey
     * 取 Hbase表monitor_decision_flow中对应所有数据
     * （主要用到决策流的快照snopshot列和执行过程process列中数据用于展示）
     * @param param
     * @return
     */
    List<?> getResultDecisionFlowDetail(DecisionFlowRequestDTO param);

    /**
     * 根据hbaseRowKey
     * 取Hbase表monitor_node 中对应的所有数据
     * (主要用到节点监控信息中快照snopshot列和执行结果result 用于 节点监控信息展示)
     * @param param
     * @return
     */
    List<?> getResultNodeDetail(DecisionFlowRequestDTO param);

    /**
     * 根据hbaseRowkey
     * 取Hbase表monitor_strategy 中对应的所有数据
     * (主要用到策略监控信息中快照snopshot列和执行结果result 用于 策略监控信息展示)
     * @param param
     * @return
     */
    List<?> getResultStrategyDetail(DecisionFlowRequestDTO param);
}
