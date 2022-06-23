package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.service.datax.runner.CommonService;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.util.runner.JevalUtil;
import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationException;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonService commonService;

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        logger.info("start【获取分组节点指标】GroupNode.getNodeField engineNode:{},inputParam:{}", JSONObject.toJSONString(engineNode), JSONObject.toJSONString(inputParam));
        JSONObject jsonObject = JSONObject.parseObject(engineNode.getNodeScript());
        JSONArray array = jsonObject.getJSONArray("fields");
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject input = array.getJSONObject(i);
            Object fieldId = input.get("fieldId");
            if(fieldId != null && !"".equals(fieldId.toString())){
                ids.add(Long.valueOf(fieldId.toString()));
            }
        }
        commonService.getFieldByIds(ids, inputParam);
    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        JSONObject jsonScript = JSONObject.parseObject(engineNode.getNodeScript());
        //监控中心--节点信息记录(不需要策略层面的监控)
        outMap.put("nodeSnapshot",JSONObject.parse(engineNode.getNodeJson()));
        JSONObject nodeInfo = new JSONObject();
        nodeInfo.put("engineNode",engineNode);
        nodeInfo.put("nodeId",engineNode.getNodeId());
        nodeInfo.put("nodeName",engineNode.getNodeName());
        nodeInfo.put("nodeType",engineNode.getNodeType());
        outMap.put("nodeInfo",nodeInfo);
        try {
            String nextNode = handleClassify(jsonScript, inputParam);
            outMap.put("nextNode", nextNode);
            JSONObject result = new JSONObject();
            result.put("nodeResult",nextNode);
            outMap.put("nodeResult",result);
        } catch (EvaluationException e) {
            e.printStackTrace();
            logger.error("请求异常", e);
        }
    }

    private static String handleClassify(JSONObject jsonScript, Map<String, Object> inputParam) throws EvaluationException {
        JSONArray conditions = jsonScript.getJSONArray("conditions");
        JSONArray fields = jsonScript.getJSONArray("fields");
        Map<String, Object> variablesMap = new HashMap<>();
        variablesMap.putAll(inputParam);
        Map<String, Integer> fieldsMap = new HashMap<>();
        for(int i = 0; i < fields.size(); i++){
            JSONObject jsonObject = fields.getJSONObject(i);
            fieldsMap.put(jsonObject.getString("fieldCode"), jsonObject.getIntValue("valueType"));
        }
        JevalUtil.convertVariables(fieldsMap, variablesMap);

        String nextNode = "";
        if (conditions == null || conditions.isEmpty()) {
            //TODO 如果为空，如何处理
            return nextNode;
        } else {
            int size = conditions.size();
            boolean flag = false;
            JSONObject formula = null;
            for (int i = 0; i < size; i++) {
                formula = conditions.getJSONObject(i);
                //公式为空，则为else条件分支
                if (CommonConst.STRING_EMPTY.equals(formula.getString("formula"))) {
                    //else条件
                    if (nextNode.equals(CommonConst.STRING_EMPTY)) {
                        nextNode = formula.getString("nextNode");
                    }
                } else {
                    //正常条件分支
                    flag = JevalUtil.evaluateBoolean(formula.getString("formula"), variablesMap);
                    if (flag) {
                        nextNode = formula.getString("nextNode");
                        break;
                    }
                }
            }
            return nextNode;
        }
    }
}
