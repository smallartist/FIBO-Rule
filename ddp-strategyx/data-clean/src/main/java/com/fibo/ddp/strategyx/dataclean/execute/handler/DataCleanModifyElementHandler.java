package com.fibo.ddp.strategyx.dataclean.execute.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanCondition;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanOutput;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.utils.constant.strategyx.RuleConst;
import com.fibo.ddp.common.utils.util.strategyx.DataCleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DataCleanModifyElementHandler {
    public boolean conditionHandler(DataCleanCondition condition, Map<String, Object> input, JSONObject intputObject, JSONObject forObject, JSONObject collectionObject) {
        boolean result = executeListCondition(condition, input, intputObject, forObject, collectionObject);
        return result;
    }

    private boolean executeListCondition(DataCleanCondition condition, Map<String, Object> input, JSONObject intputObject, JSONObject forObject, JSONObject collectionObject) {
        Integer conditionType = condition.getConditionType();
        boolean result = false;
        switch (conditionType) {
            //关系条件节点 &&和||
            case RuleConst.RELATION_CONDITION:
                result = executeRelation(condition, input, intputObject, forObject, collectionObject);
                break;
            //表达式条件节点
            case RuleConst.EXPRESSION_CONDITION:
                result = executeExpression(condition, input, intputObject, forObject, collectionObject);
                break;
        }
        return result;
    }

    private boolean executeExpression(DataCleanCondition condition, Map<String, Object> input, JSONObject intputObject, JSONObject forObject, JSONObject collectionObject) {
        Object opValue = null;
        String opType = condition.getOpType();
        switch (opType) {
            case "original": // 源数据
                opValue = DataCleanUtils.getObjByKeyAndJson(intputObject, condition.getOpKey());
                break;
            case "data_op": // 循环对象元素
                opValue = DataCleanUtils.getObjByKeyAndJson(forObject, condition.getOpKey());
                break;
            case "handle_collection": // 集合元素
                opValue = DataCleanUtils.getObjByKeyAndJson(collectionObject, condition.getOpKey());
                break;
        }

        //条件判断输入的值
        List<JSONObject> list = new ArrayList<>();
        list.add(collectionObject);
        Object variableValue = ExecuteUtils.getValueByKey(condition.getVariableType(), input, condition.getVariableValue(), list);
        //操作符
        String operator = condition.getOperator();
        return ExecuteUtils.getCondResult(operator, opValue, variableValue);
    }

    private boolean executeRelation(DataCleanCondition condition, Map<String, Object> input, JSONObject intputObject, JSONObject forObject, JSONObject collectionObject) {
        //获取关系逻辑
        String logical = condition.getLogical();
        //处理子逻辑
        List<DataCleanCondition> children = condition.getChildren();

        boolean result = false;
        switch (logical) {
            case "||":
                result = false;
                for (DataCleanCondition child : children) {
                    boolean childResult = executeListCondition(child, input, intputObject, forObject, collectionObject);
                    if (childResult) {
                        return true;
                    }
                }
                break;
            case "&&":
                result = true;
                for (DataCleanCondition child : children) {
                    boolean childResult = executeListCondition(child, input, intputObject, forObject, collectionObject);
                    if (!childResult) {
                        return false;
                    }
                }
                break;
        }
        return result;
    }

    public boolean outputListHandler(List<DataCleanOutput> outputList, Map<String, Object> localParamMap, Map<String, Object> outMap, JSONObject collectionObject) {
        if (outputList == null || outputList.isEmpty()) {
            return true;
        }
        for (DataCleanOutput outputField : outputList) {
            outputHandler(outputField,localParamMap, outMap, collectionObject);
        }
        return true;
    }

    public boolean outputHandler(DataCleanOutput output, Map<String, Object> localParamMap, Map<String, Object> outMap, JSONObject collectionObject) {
        String outputKey = output.getOutputKey();
        Object result = null;
        //处理自定义内容
        if (StringUtils.isNotBlank(output.getOutputValue())) {
            result = ExecuteUtils.getValueByKeyYiHao(output.getVariableType(), localParamMap, output.getOutputValue(), collectionObject);
        }
        outMap.put(outputKey, result);

        // todo price修改
        JSONArray channels = collectionObject.getJSONArray("channels");
        JSONArray nightlyRates = channels.getJSONObject(0).getJSONArray("nightlyRates");
        nightlyRates.getJSONObject(0).put("price", result);
        return true;
    }

}
