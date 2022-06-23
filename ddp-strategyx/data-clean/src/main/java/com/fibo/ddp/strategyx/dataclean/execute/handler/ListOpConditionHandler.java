package com.fibo.ddp.strategyx.dataclean.execute.handler;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanFilterCondition;
import com.fibo.ddp.common.utils.constant.strategyx.RuleConst;
import com.fibo.ddp.common.utils.util.runner.NumUtils;
import com.fibo.ddp.common.utils.util.strategyx.DataCleanUtils;
import com.fibo.ddp.strategyx.dataclean.execute.consts.ListOpConst;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ListOpConditionHandler {
    public JSONObject conditionHandler(DataCleanFilterCondition condition, Map<String, Object> input, List<JSONObject> list) {
        JSONObject result = executeListCondition(condition, input, list);
        return result;
    }

    private JSONObject executeListCondition(DataCleanFilterCondition condition, Map<String, Object> input, List<JSONObject> list) {
        Integer conditionType = condition.getConditionType();
        JSONObject result = null;
        switch (conditionType) {
            //关系条件节点 &&和||
            case RuleConst.RELATION_CONDITION:
                result = executeRelation(condition, input, list);
                break;
            //表达式条件节点
            case RuleConst.EXPRESSION_CONDITION:
                result = executeExpression(condition, input, list);
                break;
        }
        return result;
    }

    private JSONObject executeExpression(DataCleanFilterCondition condition, Map<String, Object> input, List<JSONObject> list) {
        Object opValue = getOpValueByKey(condition.getOpKey(), condition.getOperator(), list);
        return (JSONObject) opValue;
    }

    private JSONObject executeRelation(DataCleanFilterCondition condition, Map<String, Object> input, List<JSONObject> list) {
        //获取关系逻辑
        String logical = condition.getLogical();
        //处理子逻辑
        List<DataCleanFilterCondition> children = condition.getChildren();

        switch (logical) {
            case "||":
                for (DataCleanFilterCondition child : children) {
                    if("min".equals(child.getOperator())){
                        JSONObject childResult = executeListCondition(child, input, list);
                        return childResult;
                    }
                }
                break;
            case "&&":
                for (DataCleanFilterCondition child : children) {
                    if("min".equals(child.getOperator())){
                        JSONObject childResult = executeListCondition(child, input, list);
                        return childResult;
                    }
                }
                break;
        }
        return null;
    }

    private Object getOpValueByKey(String opKey, String opType, List<JSONObject> list) {
        //确定操作是根据哪个字段的
        Comparator<JSONObject> comparator = Comparator.comparing(item -> {
            return DataCleanUtils.getObjByKeyAndJson(item, opKey).toString();
        });
        Object result = null;
        switch (opType) {
            case ListOpConst
                    .OpType.COUNT:
                int count = list.size();
                result = count;
                break;
            case ListOpConst
                    .OpType.DISTINCT_COUNT:
                int distinctCount = list.stream().collect(Collectors.collectingAndThen(Collectors.
                        toCollection(() -> new TreeSet<>(comparator)), ArrayList::new)).size();
                result = distinctCount;
                break;
            case ListOpConst
                    .OpType.MAX:
                Optional<JSONObject> maxOptional = list.stream().max(comparator);
                if (maxOptional.isPresent()) {
                    Object max = DataCleanUtils.getObjByKeyAndJson(maxOptional.get(), opKey);
                    result = max;
                }
                break;
            case ListOpConst
                    .OpType.MIN:
                Optional<JSONObject> minOptional = list.stream().min(comparator);
                if (minOptional.isPresent()) {
//                    Object min = DataCleanUtils.getObjByKeyAndJson(minOptional.get(), opKey);
//                    result = min;
                    result = minOptional.get();
                }
                break;
            case ListOpConst
                    .OpType.AVG:
                double avg = list.stream().mapToDouble(item -> {
                    return NumUtils.toDouble(DataCleanUtils.getObjByKeyAndJson(item, opKey));
                }).average().getAsDouble();
                result = avg;
                break;
            case ListOpConst
                    .OpType.LIST_ELEMENT:
                result = list.stream().map(item -> {
                    return DataCleanUtils.getObjByKeyAndJson(item, opKey);
                }).collect(Collectors.toList());
                break;
            case ListOpConst
                    .OpType.CUSTOM:
                break;
        }
        return result;
    }
}
