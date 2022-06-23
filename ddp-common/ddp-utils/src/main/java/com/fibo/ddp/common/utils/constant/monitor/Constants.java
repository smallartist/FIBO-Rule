package com.fibo.ddp.common.utils.constant.monitor;

/**
 * 常量定义
 */
public class Constants {
    /**
     *<h2> UserInfo hbase table</h2>
     */
    public class UserInfoTable{
        /**
         * 表名
         */
        public static final String TABLE_NAME = "user_info";
        /**
         *基本信息列族
         */
        public static final String FAMILY_BASE_INFO="base_info";
        /**
         * 列
         */
        public static final String BASEINFO="baseInfo";
    }
    /**
     *<h2> monitor_decision_flow hbase table</h2>
     * 决策流监控
     */
    public class MonitorDecisionFlow{
        /**
         * 表名
         */
        public static final String TABLE_NAME = "monitor_decision_flow";
        /**
         *决策流监控信息（列族）
         */
        public static final String MONITOR_INFO = "monitor_info";
        /**
         *入参
         */
        public static final String PARAMS = "params";
        /**
         * 快照
         */
        public static final String SNAPSHOT = "snapshot";
        /**
         * 执行过程
         */
        public static final String PROCESS = "process";
        /**
         * 决策流基本信息
         */
        public static final String BASE_INFO = "base_info";
        /**
         * 业务id
         */
        public static final String BUSINESS_ID = "business_id";
        /**
         *决策流名称
         */
        public static final String ENGINE_NAME = "engine_name";
        /**
         * 决策流版本id
         */
        public static final String ENGINE_VERSION_ID = "engine_version_id";
        /**
         * 引擎版本信息
         */
        public static final String ENGINE_INFO = "engine_info";
    }

    /**
     *<h2> monitor_node hbase table</h2>
     * 节点监控
     */
    public class MonitorNode{
        /**
         * 表名
         */
        public static final String TABLE_NAME = "monitor_node";
        /**
         *节点监控信息（列族）
         */
        public static final String MONITOR_INFO = "monitor_info";
        /**
         *入参
         */
        public static final String PARAMS = "params";
        /**
         * 快照
         */
        public static final String SNAPSHOT = "snapshot";
        /**
         * 节点输出结果
         */
        public static final String RESULT = "result";
        /**
         * 节点基本信息
         */
        public static final String BASE_INFO = "base_info";
        /**
         * 业务id
         */
        public static final String BUSINESS_ID = "business_id";
        /**
         *节点名称
         */
        public static final String NODE_ID = "node_id";
        /**
         *节点名称
         */
        public static final String NODE_NAME = "node_name";
        /**
         * 节点类型
         */
        public static final String NODE_TYPE = "node_type";
        /**
         * 节点基本信息
         */
        public static final String NODE_INFO = "node_info";
        /**
         * 决策流版本id
         */
        public static final String ENGINE_VERSION_ID = "engine_version_id";
        /**
         * 引擎版本信息
         */
        public static final String ENGINE_INFO = "engine_info";
    }

    /**
     *<h2> monitor_strategy hbase table</h2>
     * 策略监控
     */
    public class MonitorStrategy{
        /**
         * 表名
         */
        public static final String TABLE_NAME = "monitor_strategy";
        /**
         *策略监控信息（列族）
         */
        public static final String MONITOR_INFO = "monitor_info";
        /**
         *入参
         */
        public static final String PARAMS = "params";
        /**
         * 快照
         */
        public static final String SNAPSHOT = "snapshot";
        /**
         * 策略输出结果
         */
        public static final String RESULT = "result";
        /**
         * 策略基本信息
         */
        public static final String BASE_INFO = "base_info";
        /**
         * 业务id
         */
        public static final String BUSINESS_ID = "business_id";
        /**
         *策略id
         */
        public static final String STRATEGY_ID = "strategy_id";
        /**
         *策略名称
         */
        public static final String STRATEGY_NAME = "strategy_name";
        /**
         *策略类型
         */
        public static final String STRATEGY_TYPE = "strategy_type";
        /**
         *节点id
         */
        public static final String NODE_ID = "node_id";
        /**
         *节点类型
         */
        public static final String NODE_TYPE = "node_type";
        /**
         * 决策流版本id
         */
        public static final String ENGINE_VERSION_ID = "engine_version_id";
    }

    /**
     *<h2> monitor_strategy hbase table</h2>
     * 全量指标记录
     */
    public class RunnerFeatureRecord{
        /**
         * 表名
         */
        public static final String TABLE_NAME = "runner_feature_record";
        /**
         *执行前指标集合（列族）
         */
        public static final String BEFORE_FEATURE = "before_feature";
        /**
         *执行后指标集合（列族）
         */
        public static final String AFTER_FEATURE = "after_feature";
        /**
         * 基本信息（列族）
         */
        public static final String BASE_INFO = "base_info";
        /**
         * 业务id
         */
        public static final String BUSINESS_ID = "business_id";
        /**
         *引擎id
         */
        public static final String ENGINE_ID = "engine_id";

        /**
         *引擎名称
         */
        public static final String ENGINE_NAME = "engine_name";

        /**
         *组织id
         */
        public static final String ORGAN_ID = "organ_id";
        /**
         * 决策流版本id
         */
        public static final String ENGINE_VERSION_ID = "engine_version_id";
    }
}
