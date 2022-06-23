package com.fibo.ddp.strategyx.dataclean.execute.handler;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanFilterCondition;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.utils.constant.strategyx.RuleConst;
import com.fibo.ddp.common.utils.util.strategyx.DataCleanUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataCleanFilterConditionHandler {
    public List<JSONObject> filterByCondition(List<JSONObject> list, DataCleanFilterCondition filterCondition){

        if (filterCondition==null||filterCondition.getChildren()==null||filterCondition.getChildren().isEmpty()) {
            return list;
        }
        //过滤框架
        List<JSONObject> result = list.stream().filter(item->{
            return executeCondition(item,filterCondition);
        }).collect(Collectors.toList());
        return result;
    }

    public boolean executeCondition(JSONObject json,DataCleanFilterCondition filterCondition){
        Integer conditionType = filterCondition.getConditionType();
        boolean result = false;
        switch (conditionType) {
            //关系条件节点 &&和||
            case RuleConst.RELATION_CONDITION:
                result = executeRelation(filterCondition, json);
                break;
            //表达式条件节点
            case RuleConst.EXPRESSION_CONDITION:
                result = executeExpression(filterCondition, json);
                break;
        }
        return result;
    }

    private boolean executeRelation(DataCleanFilterCondition condition, JSONObject json) {
        //获取关系逻辑
        String logical = condition.getLogical();
        //处理子逻辑
        List<DataCleanFilterCondition> children = condition.getChildren();

        boolean result = false;
        switch (logical) {
            case "||":
                result = false;
                for (DataCleanFilterCondition child : children) {
                    boolean childResult = executeCondition(json,child);
                    if (childResult) {
                        return true;
                    }
                }
                break;
            case "&&":
                result = true;
                for (DataCleanFilterCondition child : children) {
                    boolean childResult = executeCondition(json,child);
                    if (!childResult) {
                        return false;
                    }
                }
                break;
        }
        return result;
    }

    private boolean executeExpression(DataCleanFilterCondition condition, JSONObject json) {
        Object condKey = DataCleanUtils.getObjByKeyAndJson(json, condition.getOpKey());
        Object condValue = null;
        String condValueStr = condition.getVariableValue();
        String operator = condition.getOperator();
        switch (operator){
            case "in":
            case "not in":
                condValue  = new ArrayList<>();
                Collections.addAll((List)condValue,condValueStr.split(","));
                break;
            case "black_list":
                condKey = 1;
                operator= "==";
                condValue = ExecuteUtils.getValueByKey(condition.getVariableType(),new HashMap<>(),condition.getVariableValue(), Arrays.asList(json));
                break;
            case "not_black_list":
                condKey = 0;
                operator= "==";
                condValue = ExecuteUtils.getValueByKey(condition.getVariableType(),new HashMap<>(),condition.getVariableValue(), Arrays.asList(json));
                break;
            default:
                condValue = condValueStr;
        }
        return ExecuteUtils.getCondResult(operator, condKey, condValue);

    }
}
