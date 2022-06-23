package com.fibo.ddp.common.service.enginex.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.constant.enginex.EngineOperator;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class EngineNodeUtilV2 {

    //校验规则集节点是否选择了终止条件的规则
    public static boolean checkRuleNodeUnTerminal(EngineNode node, JSONObject terminationInfo){
        // 终止规则选择后，终止条件为必填
//        if (node.getNodeType()==2){
            //规则集类型
            JSONArray selectedRule = terminationInfo.getJSONArray("selectedRule");
            if(selectedRule == null || selectedRule.isEmpty()){
                return true;
            }
//        }
        return false;
    }

    // 规则集表达式转换
    public static void convertRule(EngineNode node) {
        if(StringUtil.isBlank(node.getNodeJson())){
            return;
        }
        // 初始化nodeScript
        JSONObject nodeScript = JSONObject.parseObject(node.getNodeJson());
        node.setNodeScript(nodeScript.toJSONString());

        JSONObject nodeJson = JSONObject.parseObject(node.getNodeJson());
        JSONObject terminationInfo = nodeJson.getJSONObject("terminationInfo");

//        JSONArray selectedRule = terminationInfo.getJSONArray("selectedRule");
        // 对于规则集节点终止规则选择后，终止条件为必填
        if(terminationInfo==null||checkRuleNodeUnTerminal(node,terminationInfo)){
            return;
        }
        JSONArray conditions = terminationInfo.getJSONArray("conditions");
        if(conditions == null || conditions.isEmpty()){
            return;
        }
        Map<String,Integer> fieldTypeMap = new HashMap<>();
        StringBuffer formula = new StringBuffer();
        for(int i = 0; i < conditions.size(); i++){
            JSONObject jsonObject = conditions.getJSONObject(i);
            if (StringUtils.isBlank(jsonObject.getString("operator"))) {
                continue;
            }
            fieldTypeMap.put(jsonObject.getString("fieldCode"),jsonObject.getInteger("valueType"));
            formula.append(convertRuleTerminalFormula(jsonObject));
            // 最后一个不拼接
            if(i < conditions.size() - 1){
                formula.append(jsonObject.getString("relativeOperator"));
            }
        }

        // 覆盖nodeScript的conditions
        JSONObject scriptTerminationInfo = nodeScript.getJSONObject("terminationInfo");
        scriptTerminationInfo.put("conditions", formula.toString());
        scriptTerminationInfo.put("fieldTypeMap", fieldTypeMap);
        node.setNodeScript(nodeScript.toJSONString());
    }

    // 规则集终止条件表达式转换
    private static String convertRuleTerminalFormula(JSONObject formula) {
        String fieldCode = formula.getString("fieldCode");
        String operator = formula.getString("operator");
        Object value = formula.get("value");
        int valueType = formula.getInteger("valueType");

        StringBuffer expression = new StringBuffer();

        expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);  // "("
        switch (valueType) {
            case 1:
                expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT + fieldCode + EngineOperator.OPERATOR_VARIABLE_RIGHT);
                expression.append(operator);
                expression.append(value);
                break;
            case 2:
                operator = operator.replace("==", "equals")
                        .replace("!=", "notEquals");

                expression.append(operator);
                expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);  // "("
                expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT + fieldCode + EngineOperator.OPERATOR_VARIABLE_RIGHT);
                expression.append(CommonConst.SYMBOL_COMMA);  // ","
                expression.append("'" + value + "'");  // "'value'"
                expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);  // ")"
                break;
            default:
                break;
        }
        expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);  // ")"

        return expression.toString();
    }

    // 处理分群 V2  设置节点的 nodeScript
    public static void convertClassifyV2(EngineNode node) {

        // 读取 nodeJson
        String nodeJson = node.getNodeJson();

        if (StringUtil.isValidStr(nodeJson)) {

            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(nodeJson);
            } catch (com.alibaba.fastjson.JSONException e) {
                // 此处可能会报 JSON转换异常
                throw new ApiException(ErrorCodeEnum.JSON_CAST_EXCEPTION.getCode(), ErrorCodeEnum.JSON_CAST_EXCEPTION.getMessage());
            }

            if (jsonObject != null && !jsonObject.isEmpty()) {
                JSONArray conditions = jsonObject.getJSONArray("conditions");
                JSONArray fields = jsonObject.getJSONArray("fields");

                if (null != conditions && !conditions.isEmpty()) {
                    JSONArray conditionArray = processClassifyConditionsV2(conditions, fields);
                    JSONObject resultjson = new JSONObject();

                    resultjson.put("fields", fields);
                    resultjson.put("conditions", conditionArray);

                    node.setNodeScript(resultjson.toString());  // 设置 nodeScript
                }
            }
        }
    }


    // 处理分群条件
    private static JSONArray processClassifyConditionsV2(JSONArray conditions, JSONArray fields) {

        JSONArray resultArray = new JSONArray();  // conditions的内容：数组
        JSONObject condition = null;
        JSONObject resultJson = null;

        String nextNode = "";

        // 循环 每一个分组
        for (int i = 0; i < conditions.size(); i++) {

            condition = conditions.getJSONObject(i);

            //获取nextNode,如果nextNode为空，则跳过  { 连线的时候才设置 }
            nextNode = condition.getString("nextNode");

            if (null == nextNode || CommonConst.STRING_EMPTY.equals(nextNode)) {
                continue;
            } else {

                // 开始解析 此分组里面的 formulas
                JSONArray formulas = condition.getJSONArray("formulas");
                if (formulas != null && !formulas.isEmpty()) {

                    resultJson = new JSONObject();
                    resultJson.put("nextNode", nextNode);
                    resultJson.put("formula", processClassifyFormulasV2(formulas, fields));  // 设置nodeScript中formula的内容
                    resultArray.add(resultJson);
                } else {
                    resultJson = new JSONObject();
                    resultJson.put("nextNode", nextNode);
                    resultJson.put("formula", "");
                    resultArray.add(resultJson);
                }
            }
        }
        return resultArray;
    }


    // 处理分群公式（设置nodeScript中formula的内容）
    private static String processClassifyFormulasV2(JSONArray formulas, JSONArray fields) {

        int size = formulas.size();

        if (size == 1) {
            return convertFormula(formulas.getJSONObject(0), fields);
        }

        StringBuffer sb = new StringBuffer();
        JSONObject formula = null;

        for (int i = 0; i < size; i++) {
            formula = formulas.getJSONObject(i);

            sb.append(convertFormula(formula, fields));
            // 最后一个不拼接 与或 连接符
            if (i < size - 1) {
                sb.append(formula.getString("relative_operator"));
            }
        }
        return sb.toString();
    }


    // 转化公式  返回示例：(#{versionCode} >= 18)
    private static String convertFormula(JSONObject formula, JSONArray fields) {
        String fieldCode = formula.getString("field_code");
        String operator = formula.getString("operator");
        Object value = formula.get("value");

        int valueType = 0;
        for(int i = 0; i < fields.size(); i++){
            JSONObject jsonObject = fields.getJSONObject(i);
            if(jsonObject.getString("fieldCode").equals(fieldCode)){
                valueType = jsonObject.getIntValue("valueType");
            }
        }

        StringBuffer expression = new StringBuffer();

        expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);  // "("
        switch (valueType) {
            case 1:
                expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT + fieldCode + EngineOperator.OPERATOR_VARIABLE_RIGHT);
                expression.append(operator);
                expression.append(value);
                break;
            case 2:

                operator = operator.replace("==", "equals")
                        .replace("!=", "notEquals");

                expression.append(operator);
                expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);  // "("
                expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT + fieldCode + EngineOperator.OPERATOR_VARIABLE_RIGHT);
                expression.append(CommonConst.SYMBOL_COMMA);  // ","
                expression.append("'" + value + "'");  // "'value'"
                expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);  // ")"
                break;
            default:
                break;
        }
        expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);  // ")"

        return expression.toString();
    }


    // 特殊处理字符串函数(等于不等于,包含不包含)
    private static String convertOperator(String fieldCode, String operator, Object value) {
        StringBuffer sb = new StringBuffer();

        // contians, notContains, equals, notEquals
        // 示例： ....  或  fieldCode==value
        if (fieldCode.equalsIgnoreCase(EngineOperator.OPERATOR_CONTAINS_STRING)
                || fieldCode.equalsIgnoreCase(EngineOperator.OPERATOR_UNCONTAINS_STRING)
                || fieldCode.equalsIgnoreCase(EngineOperator.OPERATOR_EQUALS_STRING)
                || fieldCode.equalsIgnoreCase(EngineOperator.OPERATOR_UNEQUALS_STRING)) {

            sb.append(operator).append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
            sb.append(fieldCode).append(CommonConst.SYMBOL_COMMA).append(value).append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
        } else {
            sb.append(fieldCode).append(operator).append(value);
        }
        return sb.toString();
    }


    /**
     * 处理决策选项
     *
     * @param node
     */
    public static void convertDecisionV2(EngineNode node) {
        String nodeJson = node.getNodeJson();

        if (nodeJson == null || nodeJson.trim().length() == 0) {
            return;
        }

        // JSONObject jsonObject = JSONObject.parseObject(nodeJson);
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(nodeJson);
        } catch (com.alibaba.fastjson.JSONException e) {
            // 此处可能会报 JSON转换异常
            throw new ApiException(ErrorCodeEnum.JSON_CAST_EXCEPTION.getCode(), ErrorCodeEnum.JSON_CAST_EXCEPTION.getMessage());
        }

        handleDecisionVariable(node, jsonObject);

/*
        if (jsonObject.containsKey("inputs")) {
            //获取变量个数
            int inputs = jsonObject.getInteger("inputs");
            switch (inputs) {
                case 1:
                    //单个决策变量
                    handleSingleDecisionVariable(node, jsonObject);
                    break;
                case 2:
                    //两个决策变量
                    handleDoubleDecisionVariables(node, jsonObject);
                    break;
                case 3:
                    //三个或以上决策变量
                    handleMoreThanTripleDecisionVariables(node, jsonObject);
                    break;
                default:
                    break;
            }
        }
*/


    }

    private static void handleDecisionVariable(EngineNode engineNode, JSONObject jsonObject) {

        JSONObject resultJson = new JSONObject();
        JSONArray realConditions = new JSONArray();

        int conditionType = jsonObject.getIntValue("condition_type");

        if (conditionType == 2) {
            // conditionType 为 2 ，nodeJson 直接放入 nodeScript
            engineNode.setNodeScript(engineNode.getNodeJson());
        } else if (conditionType == 1) {

            // 获取条件
            JSONArray conditions = jsonObject.getJSONArray("conditions");

            if (null != conditions && !conditions.isEmpty()) {
                int size = conditions.size();
                JSONObject condition = null;

                for (int i = 0; i < size; i++) {
                    condition = conditions.getJSONObject(i);
                    // 获取formula
                    JSONArray subArray = condition.getJSONArray("formula");
                    if (null != subArray && !subArray.isEmpty()) {
                        JSONObject result = new JSONObject();
                        result.put("result", condition.get("result"));
                        result.put("resultKey", condition.get("resultKey"));
                        int subSize = subArray.size();
                        JSONObject subJson = null;
                        StringBuffer expression = new StringBuffer();

                        for (int j = 0; j < subSize; j++) {
                            subJson = subArray.getJSONObject(j);
                            String fieldCode = subJson.getString("field_code");
                            // 字段存值类型,待选(0),数值型(1),字符型(2),枚举型(3),小数型(4)
                            int type = subJson.getInteger("valueType");

                            String operator = subJson.getString("operator");

                            expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);  // "("
                            switch (type) {
                                case 1:
                                    expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT + fieldCode + EngineOperator.OPERATOR_VARIABLE_RIGHT);
                                    expression.append(operator);
                                    expression.append(subJson.getString("result"));
                                    break;
                                case 2:

                                    operator = operator.replace("==", "equals")
                                            .replace("!=", "notEquals");

                                    expression.append(operator);
                                    expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);  // "("
                                    expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT + fieldCode + EngineOperator.OPERATOR_VARIABLE_RIGHT);
                                    expression.append(CommonConst.SYMBOL_COMMA);  // ","
                                    expression.append("'" + subJson.getString("result") + "'");  // "'value'"
                                    expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);  // ")"
                                    break;
                                default:
                                    break;
                            }
                            expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);  // ")"

                            if (j < subSize - 1) {
                                expression.append(subJson.getString("sign"));
                            }

                            /*
                            if (j != 0) {
                                expression.append(CommonConst.SYMBOL_BLANK);  // " "
                                if (EngineOperator.OPERATOR_AND_STRING_RELATION.equalsIgnoreCase(subArray.getJSONObject(j).getString("sign"))) {  // "AND"
                                    expression.append(EngineOperator.OPERATOR_AND_RELATION);  // "&&"
                                } else if (EngineOperator.OPERATOR_OR_STRING_RELATION.equalsIgnoreCase(subArray.getJSONObject(j).getString("sign"))) {  // "OR"
                                    expression.append(EngineOperator.OPERATOR_OR_RELATION);  // "||"
                                }
                                expression.append(CommonConst.SYMBOL_BLANK);  // " "
                            }

                            expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);  // "("
                            String operator = subJson.getString("operator");

                            if (jsonObject.getJSONArray("input").getJSONObject(0).getIntValue("field_type") == 2) {  // 数值型
                                expression.append(operator);  // ">", "<","==","!="
                                expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);  // "("
                                // "#{versionCode}"
                                expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT + subJson.getString("field_code") + EngineOperator.OPERATOR_VARIABLE_RIGHT);
                                expression.append(CommonConst.SYMBOL_COMMA);  // ","
                                expression.append("'" + subJson.get("result") + "'");
                                expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);  // ")"
                            } else {
                                expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT + subJson.getString("field_code") + EngineOperator.OPERATOR_VARIABLE_RIGHT);
                                expression.append(CommonConst.SYMBOL_BLANK);  // " "
                                expression.append(operator);
                                expression.append(CommonConst.SYMBOL_BLANK);  // " "
                                expression.append(subJson.get("result"));
                            }
                            expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);  // ")"
                            */

                        }

                        result.put("formula", expression.toString());
                        realConditions.add(result);
                    }
                }

                // resultJson.put("inputs", jsonObject.getIntValue("inputs"));
            }
            resultJson.put("condition_type", jsonObject.getIntValue("condition_type"));
            resultJson.put("input", jsonObject.getJSONArray("input"));
            resultJson.put("output", jsonObject.getJSONObject("output"));
            resultJson.put("conditions", realConditions);
            engineNode.setNodeScript(resultJson.toString());
        }
    }

}
