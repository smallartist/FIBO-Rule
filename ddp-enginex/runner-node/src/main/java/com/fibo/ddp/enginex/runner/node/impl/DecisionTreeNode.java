package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.runner.ExpressionParam;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeDetail;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeDetailCondition;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVersionVo;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVo;
import com.fibo.ddp.common.service.datax.runner.CommonService;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeService;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DecisionTreeNode implements EngineRunnerNode {
    @Autowired
    private DecisionTreeService decisionTreeService;
    @Autowired
    private CommonService commonService;

    private List<Long> getExecuteVersionIdList(EngineNode engineNode){
       return ExecuteUtils.getExecuteIdList(engineNode,"versionId");
    }

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        List<Long> list = getExecuteVersionIdList(engineNode);
        List<Long> fieldIds = new ArrayList<>();
        for (Long l : list) {
            fieldIds.addAll(decisionTreeService.getNodeFieldIds(l));
        }
        commonService.getFieldByIds(fieldIds,inputParam);
    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        //监控中心--记录节点快照信息
        if (engineNode != null && engineNode.getSnapshot() != null) {
            outMap.put("nodeSnapshot", engineNode.getSnapshot());
        }
        List<Long> list = getExecuteVersionIdList(engineNode);
        JSONObject nodeInfo = new JSONObject();
        nodeInfo.put("engineNode", engineNode);
        nodeInfo.put("nodeId", engineNode.getNodeId());
        nodeInfo.put("nodeName", engineNode.getNodeName());
        nodeInfo.put("nodeType", engineNode.getNodeType());
        outMap.put("nodeInfo", nodeInfo);
        JSONArray strategySnapshot = new JSONArray();
        for (Long versionId : list) {
            DecisionTreeVo decisionTreeVo = decisionTreeService.queryExecuteDecisionTree(null, versionId);
            if (decisionTreeVo == null) {
                continue;
            }
            //监控中心==策略层面快照信息记录
            if(decisionTreeVo.getExecuteVersion().getSnapshot()!=null){
                strategySnapshot.add(decisionTreeVo.getExecuteVersion().getSnapshot());
            }
            DecisionTreeVersionVo version = decisionTreeVo.getExecuteVersion();
            String resultFieldEn = version.getResultFieldEn();
            //执行决策表
            Object executeResult = this.executeDecisionTree(version, inputParam);
            //处理结果
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nodeId", engineNode.getNodeId());
            jsonObject.put("nodeName", engineNode.getNodeName());
            jsonObject.put("decisionTreeId", decisionTreeVo.getId());
            jsonObject.put("decisionTreeName", decisionTreeVo.getName());
            jsonObject.put("desc", version.getDescription());
            jsonObject.put("versionId", version.getId());
            jsonObject.put("versionCode", version.getVersionCode());
            if (executeResult != null) {
                jsonObject.put("result", executeResult);
                JSONObject resultField = new JSONObject();
                resultField.put(resultFieldEn, executeResult);
                inputParam.put(resultFieldEn, executeResult);
                List<JSONObject> fieldList = new ArrayList<>();
                fieldList.add(resultField);
                //处理自定义输出
                List<JSONObject> jsonObjects = decisionTreeService.setOutput(versionId, inputParam);
                fieldList.addAll(jsonObjects);
                jsonObject.put("fieldList", fieldList);
            } else {
                jsonObject.put("result", "");
                inputParam.put(resultFieldEn, "");
            }
            //将执行结果存入最终返回值
            if (outMap.containsKey("decisionTreeJson")) {
                JSONArray resultJson = (JSONArray) outMap.get("decisionTreeJson");
                resultJson.add(jsonObject);
                //监控中心==》将执行结果写入Hbase
                JSONObject nodeResult = new JSONObject();
                nodeResult.put("result", resultJson);
                outMap.put("nodeResult", nodeResult);
            } else {
                JSONArray resultJson = new JSONArray();
                resultJson.add(jsonObject);
                outMap.put("decisionTreeJson", resultJson);
                //监控中心==》将执行结果写入Hbase
                JSONObject nodeResult = new JSONObject();
                nodeResult.put("result", resultJson);
                outMap.put("nodeResult", nodeResult);
            }
            terminalCondition(engineNode,inputParam,outMap,executeResult);
        }
        //监控中心==》策略层面快照信息记录
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("snapshot",strategySnapshot);
        outMap.put("decisionTreeStrategy",jsonObject);
    }

    private Object executeDecisionTree(DecisionTreeVersionVo version, Map<String, Object> inputParam) {
        List<DecisionTreeDetail> detailList = version.getDetailList();
        for (DecisionTreeDetail decisionTreeDetail : detailList) {
            Object o = executeDecisionTreeDetail(decisionTreeDetail, inputParam);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    private Object executeDecisionTreeDetail(DecisionTreeDetail detail, Map<String, Object> paramMap) {
        //获取需要执行的条件列表
        List<DecisionTreeDetailCondition> conditionList = detail.getConditionList();
        String fieldEn = detail.getFieldEn();
        String logical = detail.getLogical();
        boolean result = false;
        //根据不通关系进行处理
        switch (logical) {
            case "||":
                result = false;
                for (DecisionTreeDetailCondition condition : conditionList) {
                    ExpressionParam expressionParam = new ExpressionParam();
                    BeanUtils.copyProperties(condition, expressionParam);
                    expressionParam.setFieldEn(fieldEn);
                    boolean expressionResult = ExecuteUtils.getExpressionResult(expressionParam, paramMap);
                    if (expressionResult) {
                        result = true;
                        break;
                    }
                }
                break;
            case "&&":
                result = true;
                for (DecisionTreeDetailCondition condition : conditionList) {
                    ExpressionParam expressionParam = new ExpressionParam();
                    BeanUtils.copyProperties(condition, expressionParam);
                    expressionParam.setFieldEn(fieldEn);
                    boolean expressionResult = ExecuteUtils.getExpressionResult(expressionParam, paramMap);
                    if (!expressionResult) {
                        result = false;
                        break;
                    }
                }
                break;
        }
        //如果本节点符合则执行后续节点
        if (result) {
            Integer type = detail.getNodeType();
            List<DecisionTreeDetail> children = detail.getChildren();
            if (type == null) {
                if (children == null || children.isEmpty()) {
                    type = 2;
                } else {
                    type = 1;
                }
            }
            if (type != null) {
                switch (type) {
                    //普通节点符合，让子节点继续执行。
                    case 1:
                        if (children != null && children.size() > 0) {
                            for (DecisionTreeDetail child : children) {
                                Object executeResult = this.executeDecisionTreeDetail(child, paramMap);
                                if (executeResult != null) {
                                    return executeResult;
                                }
                            }
                        }
                        break;
                    //叶子节点符合，返回叶子节点的值。
                    case 2:
                        String resultStr = detail.getResultValue();
                        Object resultValue = resultStr;
                        Integer variableType = detail.getVariableType();
                        if (variableType == 2) {
                            resultValue = ExecuteUtils.getObjFromMap(paramMap, resultStr);
                        } else if (variableType == 3) {
                            resultValue = ExecuteUtils.getObjFromScript(paramMap, resultStr);
                        }
                        return resultValue;
                    default:
                        break;
                }
            }
        }
        return null;
    }
    private void terminalCondition(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap,Object executeResult) {
        String resultKey = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_terminal_result";
        Map<String,Object> map = new HashMap<>();
        map.put(resultKey,executeResult);
        ExecuteUtils.terminalCondition(engineNode,inputParam,outMap, map);
    }
}
