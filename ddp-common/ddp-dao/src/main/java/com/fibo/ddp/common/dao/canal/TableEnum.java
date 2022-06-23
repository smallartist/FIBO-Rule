package com.fibo.ddp.common.dao.canal;

/**
 * 缓存数据同步表
 */
public enum TableEnum {

    /**
     * 引擎
     */
    T_ENGINE("t_engine", "id", ""),
    T_ENGINE_VERSION("t_engine_version", "version_id", "engine_id"),
    T_ENGINE_NODE("t_engine_node", "node_id", "version_id"),

    /**
     * 指标
     */
    T_FIELD("t_field", "id", ""),
    T_FIELD_INTERFACE("t_field_interface", "id", ""),
    T_FIELD_DATA_SOURCE("t_field_data_source", "id", ""),

    /**
     * 规则
     */
    T_RULE("t_rule", "id", ""),
    T_RULE_VERSION("t_rule_version", "id", "rule_id"),
    T_RULE_CONDITION("t_rule_condition", "id", "version_id"),
    T_RULE_LOOP_GROUP_ACTION("t_rule_loop_group_action", "id", "condition_for_id"),

    /**
     * 评分卡
     */
    T_SCORECARD("t_scorecard", "id", ""),
    T_SCORECARD_VERSION("t_scorecard_version", "id", "scorecard_id"),
    T_SCORECARD_DIMENSION("t_scorecard_dimension", "id", "version_id"),
    T_SCORECARD_DETAIL("t_scorecard_detail", "id", "dimension_id"),
    T_SCORECARD_DETAIL_CONDITION("t_scorecard_detail_condition", "id", "detail_id"),

    /**
     * 决策表
     */
    T_DECISION_TABLES("t_decision_tables", "id", ""),
    T_DECISION_TABLES_VERSION("t_decision_tables_version", "id", "decision_tables_id"),
    T_DECISION_TABLES_DETAIL("t_decision_tables_detail", "id", "version_id"),
    T_DECISION_TABLES_DETAIL_CONDITION("t_decision_tables_detail_condition", "id", "detail_id"),
    T_DECISION_TABLES_RESULT("t_decision_tables_result", "id", "version_id"),

    /**
     * 决策树
     */
    T_DECISION_TREE("t_decision_tree", "id", ""),
    T_DECISION_TREE_VERSION("t_decision_tree_version", "id", "decision_tree_id"),
    T_DECISION_TREE_DETAIL("t_decision_tree_detail", "id", "decision_tree_version_id"),
    T_DECISION_TREE_DETAIL_CONDITION("t_decision_tree_detail_condition", "id", "detail_id"),
    /**
     * 模型
     */
    T_MACHINE_LEARNING_MODELS("t_machine_learning_models", "id", ""),

    /**
     * 策略输出
     */
    T_STRATEGY_OUTPUT("t_strategy_output", "id", "strategy_id"),

    /**
     * 集合规则
     */
    T_LIST_OPERATION("t_list_operation","id",""),
    T_LIST_OPERATION_VERSION("t_list_operation_version","id","list_op_id"),
    T_LIST_OPERATION_BLOCK("t_list_operation_block","id","list_op_version_id"),
    T_LIST_OPERATION_CONDITION("t_list_operation_condition","id","list_op_version_id"),
    T_LIST_OPERATION_FILTER_CONDITION("t_list_operation_filter_condition","id","list_op_version_id"),
    T_LIST_OPERATION_OUTPUT("t_list_operation_output","id","list_op_version_id"),
    /**
     * 数据清洗
     */
    T_DATA_CLEAN("t_data_clean","id",""),
    T_DATA_CLEAN_VERSION("t_data_clean_version","id","data_clean_id"),
    T_DATA_CLEAN_ORIGINAL_DATA_OP("t_data_clean_original_data_op","id","data_clean_version_id"),
    T_DATA_CLEAN_BLOCK("t_data_clean_block","id","data_clean_version_id"),
    T_DATA_CLEAN_CONDITION("t_data_clean_condition","id","data_clean_version_id"),
    T_DATA_CLEAN_FILTER_CONDITION("t_data_clean_filter_condition","id","data_clean_version_id"),
    T_DATA_CLEAN_OUTPUT("t_data_clean_output","id","data_clean_version_id");
    private String tableName;
    private String primaryId;
    private String foreignId;

    TableEnum(String tableName, String primaryId, String foreignId) {
        this.tableName = tableName;
        this.primaryId = primaryId;
        this.foreignId = foreignId;
    }

    public static TableEnum getByTableName(String tableName) {
        for (TableEnum tableEnum : TableEnum.values()) {
            if (tableName.equals(tableEnum.getTableName())) {
                return tableEnum;
            }
        }
        return null;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }
}
