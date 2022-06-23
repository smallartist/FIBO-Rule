package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.service.datax.runner.CommonService;
import com.fibo.ddp.common.utils.util.runner.JevalUtil;
import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationException;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 决策选项节点
 */
@Service
public class DecisionOptionsNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonService commonService;

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        logger.info("start【获取决策选项节点指标】DecisionOptionsNode.getNodeField engineNode:{},inputParam:{}", JSONObject.toJSONString(engineNode), JSONObject.toJSONString(inputParam));
        JSONObject jsonObject = JSONObject.parseObject(engineNode.getNodeScript());
        JSONArray array = jsonObject.getJSONArray("input");
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject input = array.getJSONObject(i);
            Object fieldId = input.get("field_id");
            if(fieldId != null && !"".equals(fieldId.toString())){
                ids.add(Long.valueOf(fieldId.toString()));
            }
        }
        commonService.getFieldByIds(ids, inputParam);
    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        //监控信息--节点信息记录（不需要策略层面的监控）
        outMap.put("nodeSnapshot",JSON.parseObject(engineNode.getNodeJson()));
        JSONObject nodeInfo = new JSONObject();
        nodeInfo.put("engineNode",engineNode);
        nodeInfo.put("nodeId",engineNode.getNodeId());
        nodeInfo.put("nodeName",engineNode.getNodeName());
        nodeInfo.put("nodeType",engineNode.getNodeType());
        outMap.put("nodeInfo",nodeInfo);
        JSONObject jsonObject = JSONObject.parseObject(engineNode.getNodeScript());
        JSONArray inputArray = jsonObject.getJSONArray("input");
        List<JSONObject> inputList = JSONObject.parseArray(JSONObject.toJSONString(jsonObject.getJSONArray("input")), JSONObject.class);
        JSONArray conditionArray = jsonObject.getJSONArray("conditions");
        JSONObject outputJson = jsonObject.getJSONObject("output");

        // 变量值转义
        Map<String, Object> variablesMap = new HashMap<>();
        for (int i = 0; i < inputArray.size(); i++) {
            String input = inputArray.get(i).toString();
            JSONObject inputField = JSONObject.parseObject(input);
            String field_code = inputField.getString("field_code");
            Map<String, Integer> fieldsMap = new HashMap<>();
            fieldsMap.put(field_code, inputField.getInteger("valueType"));
            variablesMap.put(field_code, inputParam.get(field_code));
            variablesMap = JevalUtil.convertVariables(fieldsMap, variablesMap);
        }

        // 默认值处理
        String dicisionResult ="";
        String defaultValue = outputJson.getString("defaultValue");
        if (StringUtils.isNotBlank(defaultValue)){
            dicisionResult = defaultValue;
        }
        // 决策条件判断
        if(conditionArray != null && conditionArray.size() > 0){
            for (int i = 0; i < conditionArray.size(); i++) {
                JSONObject formulaJson = JSONObject.parseObject(conditionArray.getString(i));
                try {
                    boolean outfieldvalue = JevalUtil.evaluateBoolean(formulaJson.getString("formula"), variablesMap);
                    if (outfieldvalue) {
                        dicisionResult = formulaJson.getString("result");
                        // 输出结果计算
                        String result = formulaJson.getString("result");
                        if(result.contains("{") && result.contains("}")){
                            String expression = result;
                            Pattern pattern = Pattern.compile("\\{[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+\\}");
                            Matcher matcher = pattern.matcher(expression);
                            while (matcher.find()) {
                                String asName = matcher.group(0).replace("{", "").replace("}", "");
                                Optional<JSONObject> inputObj = inputList.stream().filter(item -> asName.equals(item.getString("asName"))).findFirst();
                                if(inputObj.isPresent()){
                                    String field_code = inputObj.get().getString("field_code");
                                    expression = expression.replaceAll(asName, field_code);
                                }
                            }
                            expression = expression.replaceAll("\\{", "#{");
                            Double calResult = JevalUtil.evaluateNumric(expression, variablesMap);
                            dicisionResult = calResult.toString();
                        }

                        break;
                    }
                } catch (EvaluationException e) {
                    e.printStackTrace();
                    logger.error("请求异常", e);
                }
            }
        }

        Map<String, Object> outFields = new HashMap<>();
        String outputFieldCode = outputJson.getString("field_code");
        outFields.put("fieldId", outputJson.getIntValue("field_id"));
        outFields.put("fieldName", outputJson.getString("field_name"));
        outFields.put("fieldCode", outputFieldCode);
        outFields.put("outValue", dicisionResult);
        outMap.put("result", dicisionResult);
        String key = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_result";
        inputParam.put(key, dicisionResult);
        inputParam.put(outputFieldCode, dicisionResult);

        JSONObject json = new JSONObject();
        json.put("nodeId", engineNode.getNodeId());
        json.put("nodeName", engineNode.getNodeName());
        json.put("outFields", JSONObject.parseObject(JSON.toJSONString(outFields)));
        //监控中心===》hbase中写入结果信息
        outMap.put("nodeResult",json);
        if (outMap.containsKey("decisionJson")) {
            JSONArray resultJson = (JSONArray) outMap.get("decisionJson");
            resultJson.add(json);
        } else {
            JSONArray resultJson = new JSONArray();
            resultJson.add(json);
            outMap.put("decisionJson", resultJson);
        }
    }

}
