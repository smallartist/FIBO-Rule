package com.fibo.ddp.common.utils.constant;

public class AnalyseConst {
    //统计维度（1.调用次数 engine_call,2.决策结果 decision_result,3规则命中 rule_hit 4.评分卡 scorecard 5.决策表 decision_tables 6.节点命中 NODE_HIT）
    public static final String ENGINE_CALL = "engine_call";
    public static final String DECISION_RESULT = "decision_result";
    public static final String RULE_HIT = "rule_hit";
    public static final String SCORECARD = "scorecard";
    public static final String DECISION_TABLES = "decision_tables";
//    public static final String LIST_DB = "list_db";
    public static final String NODE_HIT = "node_hit";
    public static final String[] KEYS = {ENGINE_CALL,DECISION_RESULT,RULE_HIT,SCORECARD,DECISION_TABLES,NODE_HIT};
}
