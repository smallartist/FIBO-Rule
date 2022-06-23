package com.fibo.ddp.common.model.monitor.decisionflow;

import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 决策流监控 业务抽象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorDecisionFlow {
    private String rowKey;
    /**
     * 决策流监控信息
     */
    private MonitorInfo monitorInfo;

    /**
     * 决策流基本信息
     */
    private BaseInfo baseInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonitorInfo{
        /**
         * 入参
         */
        private Map<String, Object> params;
        /**
         * 快照 (所有包含节点)
         */
        private List<EngineNode>  snapshot;
        /**
         * 执行过程 (执行节点id)
         */
        private List<String> process;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseInfo{
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
        private EngineVersion engineInfo;

    }


}
