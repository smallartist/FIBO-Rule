package com.fibo.ddp.common.model.monitor.decisionflow;

import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 决策流监控信息 数据转换抽象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorDecisionFlowDTO {
    /**
     * 入参
     */
    private Map<String, Object> params;
    /**
     * 快照 (所有包含节点)
     */
    private List<EngineNode> snapshot;
    /**
     * 执行过程 (执行节点id)
     */
    private List<String> process;



    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 决策流名称
     */
    private String  engineName;
    /**
     * 决策流版本id
     */
    private String engineVersionId;
    /**
     * 执行引擎相关版本信息
     */
//        private EngineVersionV3 engineInfo;
}
