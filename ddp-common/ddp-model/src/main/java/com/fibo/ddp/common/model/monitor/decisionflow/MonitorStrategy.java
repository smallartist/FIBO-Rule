package com.fibo.ddp.common.model.monitor.decisionflow;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 监控中心-- 策略层面 业务模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorStrategy {
    /**
     * 行键
     */
    private String rowKey;
    /**
     * 节点监控信息
     */
    private MonitorStrategy.MonitorInfo monitorInfo;

    /**
     * 节点基本信息
     */
    private MonitorStrategy.BaseInfo baseInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonitorInfo{
        /**
         * 入参
         */
        private Map<String, Object> params;
        /**
         * 快照 (策略下对应的配置)
         */
        private JSONObject snapshot;
        /**
         * 策略的输出部分
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
         * 策略id
         */
        private String strategyId;
        /**
         * 策略名称
         */
        private String  strategyName;
        /**
         * 策略类型
         */
        private String strategyType;
        /**
         *所属节点id
         */
        private String nodeId;
        /**
         *节点类型
         */
        public  String nodeType;

        /**
         * 引擎版本id
         */
        private String engineVersionId;
    }
}
