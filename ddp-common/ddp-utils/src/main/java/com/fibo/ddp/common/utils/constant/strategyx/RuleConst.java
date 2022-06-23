package com.fibo.ddp.common.utils.constant.strategyx;

public class RuleConst {
    public static final int STATUS_ENABLED = 1;//启用状态，默认
    public static final int STATUS_DEAD = 0;//停用状态
    public static final int STATUS_DELETE = -1;//删除状态


    public static final int TYPE_SYSTEM = 0;//系统规则
    public static final int TYPE_ORGAN = 1;//组织规则，默认
    public static final int TYPE_ENGINE = 2;//引擎规则

    public static final int RULE_TYPE_TERMINATION = 0;//终止
    public static final int RULE_TYPE_SCORING = 1;//计分

    public static final int RULE_AUDIT_TERMINATION = 2;//终止
    public static final int RULE_AUDIT_SCORING = 5;//继续

    // runner
    public static final int CONST_TYPE = 1;//常量
    public static final int VARIABLE_TYPE = 2;//变量

    public static final int RELATION_CONDITION = 1;//关系节点表示&&或者||
    public static final int EXPRESSION_CONDITION = 2;//表达式条件
    public static final int LOOP_CONDITION = 3;//循环条件
    public static final int LOOP_RESULT_CONDITION = 4;//循环规则条件
    public static final int CONDITION_GROUP_CONDITION = 5;//条件组节点
    public static final int CONDITION_RESULT_CONDITION = 6;//条件组节点


    public static final int LOOP_GROUP_ACTION_TYPE_SUM = 1;//循环中求和
    public static final int LOOP_GROUP_ACTION_TYPE_ASSIGNMENT = 2;//赋值

    public static final int LOOP_GROUP_ACTION_TYPE_OUT_VARIABLE = 3;//输出变量
    public static final int LOOP_GROUP_ACTION_TYPE_OUT_CONST = 4;//输出常量

    public static class ScriptType {
        public static final String GROOVY = "groovy";
        public static final String PYTHON = "python";
        public static final String JAVASCRIPT = "js";
    }
}
