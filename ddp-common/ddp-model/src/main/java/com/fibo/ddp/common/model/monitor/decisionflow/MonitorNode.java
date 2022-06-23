package com.fibo.ddp.common.model.monitor.decisionflow;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorNode {
    /**
     * 行键
     */
    private String rowKey;
    /**
     * 节点监控信息
     */
    private MonitorInfo monitorInfo;

    /**
     * 节点基本信息
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
         * 快照 (节点下对应的配置信息)
         */
        private JSONObject snapshot;
        /**
         * 节点的输出结果 (根据节点类型去获取需要记录的结果)
         */
        private JSONObject result;
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
         * 节点id
         */
        private String nodeId;
        /**
         * 节点名称
         */
        private String  nodeName;
        /**
         * 节点类型
         */
        private String nodeType;
        /**
         * 节点基本信息
         */
        private String nodeInfo;
        /**
         * 引擎版本id
         */
        private String engineVersionId;
        /**
         * 所属引擎相关版本信息
         */
        private EngineVersion engineInfo;

    }
}
