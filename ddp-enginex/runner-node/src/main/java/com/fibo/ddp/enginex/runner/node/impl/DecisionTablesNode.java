package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.InputParam;
import com.fibo.ddp.common.model.enginex.risk.Result;
import com.fibo.ddp.common.model.enginex.runner.ExpressionParam;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesDetailCondition;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesDetailVo;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesResultVo;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVersionVo;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVo;
import com.fibo.ddp.common.service.datax.runner.CommonService;
import com.fibo.ddp.common.service.datax.runner.ExecuteUtils;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesDetailService;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesService;
import com.fibo.ddp.common.service.strategyx.decisiontable.impl.DecisionTablesServiceImpl;
import com.fibo.ddp.common.utils.common.MD5;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.enginex.runner.ksession.KSessionPool;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.apache.commons.lang3.StringUtils;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class DecisionTablesNode implements EngineRunnerNode {
    private static final Logger logger = LoggerFactory.getLogger(DecisionTablesServiceImpl.class);

    @Resource
    private RedisManager redisManager;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private KSessionPool kSessionPool;
    @Resource
    private DecisionTablesService decisionTablesService;
    @Autowired
    private DecisionTablesDetailService detailService;
    @Autowired
    private CommonService commonService;

    private List<Long> getExecuteVersionIdList(EngineNode engineNode) {
        return ExecuteUtils.getExecuteIdList(engineNode, "versionId");
    }

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {
        try {
            List<Long> versionIdList = getExecuteVersionIdList(engineNode);
            //获取决策表节点的字段id
            List<Long> ids = new ArrayList<>();
            for (Long versionId : versionIdList) {
                ids.addAll(detailService.queryFieldIdByDecisionTablesVersionId(versionId));
            }
            commonService.getFieldByIds(ids, inputParam);
        } catch (Exception e) {
            logger.error("【DecisionTablesNode】,getNodeField:获取决策表指标异常");
        }
    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        //监控中心-节点信息快照记录
        if (engineNode != null && engineNode.getNodeJson() != null) {
            outMap.put("nodeSnapshot", engineNode.getNodeJson());
        }
        List<Long> versionIdList = getExecuteVersionIdList(engineNode);
        JSONObject nodeInfo = new JSONObject();
        nodeInfo.put("engineNode", engineNode);
        nodeInfo.put("nodeId", engineNode.getNodeId());
        nodeInfo.put("nodeName", engineNode.getNodeName());
        nodeInfo.put("nodeType", engineNode.getNodeType());
        outMap.put("nodeInfo", nodeInfo);
        JSONArray strategySnapshot = new JSONArray();
        for (Long versionId : versionIdList) {
            //获取决策表decisionTablesVo
            DecisionTablesVo decisionTablesVo = decisionTablesService.queryByVersionId(versionId);
            //监控中心==策略层面快照信息记录
            if (decisionTablesVo.getExecuteVersion().getSnapshot() != null) {
                strategySnapshot.add(decisionTablesVo.getExecuteVersion().getSnapshot());
            }
            DecisionTablesVersionVo version = decisionTablesVo.getExecuteVersion();
            //获取存放决策表执行结果的变量
            String resultFieldEn = version.getResultFieldEn();
            //执行决策表
            Object executeResult = this.executeDecisionTables(version, inputParam);
            //处理结果
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nodeId", engineNode.getNodeId());
            jsonObject.put("nodeName", engineNode.getNodeName());
            jsonObject.put("decisionTablesId", decisionTablesVo.getId());
            jsonObject.put("decisionTablesName", decisionTablesVo.getName());
            jsonObject.put("desc", version.getDescription());
            jsonObject.put("versionId", version.getId());
            jsonObject.put("versionCode", version.getVersionCode());
            if (executeResult != null) {
                jsonObject.put("result", executeResult);
                JSONObject resultField = new JSONObject();
                resultField.put(resultFieldEn, executeResult);
                //将执行结果按照固定格式存入参数map以供后续节点使用.
//            inputParam.put("decisionTable_"+decisionTablesId+"_"+engineNode.getNodeId(),executeResult);
                inputParam.put(resultFieldEn, executeResult);
                List<JSONObject> fieldList = new ArrayList<>();
                fieldList.add(resultField);
                //处理自定义输出
                List<JSONObject> jsonObjects = decisionTablesService.setOutput(versionId, inputParam);
                fieldList.addAll(jsonObjects);
                jsonObject.put("fieldList", fieldList);
            } else {
                jsonObject.put("result", "");
//            inputParam.put("decisionTable_"+decisionTablesId+"_"+engineNode.getNodeId(),"");
                inputParam.put(resultFieldEn, "");
            }
            //将执行结果存入最终返回值
            if (outMap.containsKey("decisionTablesJson")) {
                JSONArray resultJson = (JSONArray) outMap.get("decisionTablesJson");
                resultJson.add(jsonObject);
                //监控中心==》将执行结果写入Hbase
                JSONObject nodeResult = new JSONObject();
                nodeResult.put("result", resultJson);
                outMap.put("nodeResult", nodeResult);
            } else {
                JSONArray resultJson = new JSONArray();
                resultJson.add(jsonObject);
                outMap.put("decisionTablesJson", resultJson);
                //监控中心==》将执行结果写入Hbase
                JSONObject nodeResult = new JSONObject();
                nodeResult.put("result", resultJson);
                outMap.put("nodeResult", nodeResult);
            }
            terminalCondition(engineNode, inputParam, outMap, executeResult);
        }
        //监控中心==》策略层面快照信息记录
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("snapshot", strategySnapshot);
        outMap.put("decisionTableStrategy", jsonObject);
    }

    //执行整个决策表返回决策结果数据
    public Object executeDecisionTables(DecisionTablesVersionVo versionVo, Map<String, Object> inputParam) {
        Future<Integer> top = null;
        Future<Integer> left = null;
        //取出行列索引
        Integer row = -1;
        Integer column = -1;
        List<DecisionTablesDetailVo> leftDetailVo = versionVo.getLeftDetailVo();
        if (CollectionUtils.isEmpty(leftDetailVo)
                || (leftDetailVo.size() == 1
                && StringUtils.isBlank(leftDetailVo.get(0).getFieldEn())
                && CollectionUtils.isEmpty(leftDetailVo.get(0).getChildren()))) {
            row = 0;
        } else {
            //左侧执行
            left = threadPoolTaskExecutor.submit(new Callable<Integer>() {
                @Override
                public Integer call() {
                    return executeDecisionTablesDetail(versionVo.getLeftDetailVo(), inputParam);
                }
            });
        }
        //右侧执行
        List<DecisionTablesDetailVo> topDetailVo = versionVo.getTopDetailVo();
        if (CollectionUtils.isEmpty(topDetailVo)
                || (topDetailVo.size() == 1
                && StringUtils.isBlank(topDetailVo.get(0).getFieldEn())
                && CollectionUtils.isEmpty(topDetailVo.get(0).getChildren()))) {
            column = 0;
        } else {
            top = threadPoolTaskExecutor.submit(new Callable<Integer>() {
                @Override
                public Integer call() {
                    return executeDecisionTablesDetail(versionVo.getTopDetailVo(), inputParam);
                }
            });
        }
        try {
            if (row == -1) {
                row = left.get();
            }
            if (column == -1) {
                column = top.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //行列值均取到的进行处理
        if (row >= 0 && column >= 0) {
            //根据行列去结果集中找结果返回
            DecisionTablesResultVo resultSet = versionVo.getResultSet();
            if (row < resultSet.getRows() && column < resultSet.getColumns()) {
                String resultValue = resultSet.getResultValue();
                JSONArray array = JSON.parseArray(resultValue);
                JSONArray rowArray = JSON.parseArray(JSON.toJSONString(array.get(row)));
                return rowArray.get(column);
            }
        }
        return null;
    }

    //执行决策表的条件获取
    public Integer executeDecisionTablesDetail(List<DecisionTablesDetailVo> decisionTablesDetailList, Map<String, Object> paramMap) {

        for (DecisionTablesDetailVo decisionTablesDetailVo : decisionTablesDetailList) {
            //调用drools执行
//            int result = recursionExecuteDecisionTablesDetail(decisionTablesDetailVo, paramMap);
            //调用不使用drools执行
            int result = executeDecisionTablesDetail(decisionTablesDetailVo, paramMap);
            if (result >= 0) {
                return result;
            }
        }
        return -1;
    }

    //执行决策表详情得出位置坐标:drools执行
    public int recursionExecuteDecisionTablesDetail(DecisionTablesDetailVo decisionTablesDetailVo, Map<String, Object> paramMap) {
        StatefulKnowledgeSession kSession = null;
        String keyMd5 = null;
        try {
            //解析content
            String ruleString = decisionTablesDetailVo.getContent().replace("\\r\\n", "\r\n");
            ruleString = ruleString.replace("\\t", "\t");
            keyMd5 = CommonConst.DROOLS_KSESSION_KEY_PREFIX + MD5.GetMD5Code(ruleString);
            redisManager.set(keyMd5, ruleString, 120);

            //drools执行
            kSession = kSessionPool.borrowObject(keyMd5);
            List<Result> resultList = new ArrayList<>();
            InputParam inputParam = new InputParam();
            inputParam.setInputParam(paramMap);
            inputParam.setResult(resultList);
            FactHandle fact = kSession.insert(inputParam);
            kSession.fireAllRules();
            kSession.retract(fact);
            //获取执行结果对结果进行分析。
            List<Result> results = inputParam.getResult();
            Map<String, Object> resultMap = new HashMap<>();
            if (results != null && results.size() > 0 && results.get(0) != null && results.get(0).getMap() != null) {
                resultMap = inputParam.getResult().get(0).getMap();
            }

            //本节点命中后处理
            if (resultMap.containsKey("result")) {
                Integer type = decisionTablesDetailVo.getType();
                List<DecisionTablesDetailVo> children = decisionTablesDetailVo.getChildren();
                if (type != null) {
                    switch (type) {
                        //普通节点符合，让子节点继续执行。
                        case 1:
                            if (children != null && children.size() > 0) {
                                for (DecisionTablesDetailVo child : children) {
                                    int result = this.recursionExecuteDecisionTablesDetail(child, paramMap);
                                    if (result >= 0) {
                                        return result;
                                    }
                                }
                            }
                            break;
                        //叶子节点符合，返回叶子节点的值。
                        case 2:
                            return decisionTablesDetailVo.getIndexValue();
                        default:
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请求异常", e);
        } finally {
            if (keyMd5 != null && kSession != null) {
                kSessionPool.returnObject(keyMd5, kSession);
            }
        }
        //执行至此则不命中。
        return -1;
    }

    //不使用drools的执行
    private int executeDecisionTablesDetail(DecisionTablesDetailVo decisionTablesDetailVo, Map<String, Object> paramMap) {
        //获取需要执行的条件列表
        List<DecisionTablesDetailCondition> conditionList = decisionTablesDetailVo.getConditionList();
        String fieldEn = decisionTablesDetailVo.getFieldEn();
        String logical = decisionTablesDetailVo.getLogical();
        boolean result = false;
        //根据不通关系进行处理
        switch (logical) {
            case "||":
                result = false;
                for (DecisionTablesDetailCondition condition : conditionList) {
                    ExpressionParam expressionParam = new ExpressionParam();
                    BeanUtils.copyProperties(condition, expressionParam);
                    expressionParam.setFieldEn(fieldEn);
                   try {
                       boolean expressionResult = ExecuteUtils.getExpressionResult(expressionParam, paramMap);
                       if (expressionResult) {
                           result = true;
                           break;
                       }
                   }catch (Throwable e){
                       logger.error("【DecisionTablesNode】，runNode执行异常：expressionParam{}",expressionParam);
                       result = false;
                       break;
                   }
                }
                break;
            case "&&":
                result = true;
                for (DecisionTablesDetailCondition condition : conditionList) {
                    ExpressionParam expressionParam = new ExpressionParam();
                    BeanUtils.copyProperties(condition, expressionParam);
                    expressionParam.setFieldEn(fieldEn);
                    try {
                        boolean expressionResult = ExecuteUtils.getExpressionResult(expressionParam, paramMap);
                        if (!expressionResult) {
                            result = false;
                            break;
                        }
                    }catch (Throwable e){
                        logger.error("【DecisionTablesNode】，runNode执行异常：expressionParam{}",expressionParam);
                        result = false;
                        break;
                    }
                }
                break;
        }
        //如果本节点符合则执行后续节点
        if (result) {
            Integer type = decisionTablesDetailVo.getType();
            List<DecisionTablesDetailVo> children = decisionTablesDetailVo.getChildren();
            if (type != null) {
                switch (type) {
                    //普通节点符合，让子节点继续执行。
                    case 1:
                        if (children != null && children.size() > 0) {
                            for (DecisionTablesDetailVo child : children) {
                                int executeResult = this.executeDecisionTablesDetail(child, paramMap);
                                if (executeResult >= 0) {
                                    return executeResult;
                                }
                            }
                        }
                        break;
                    //叶子节点符合，返回叶子节点的值。
                    case 2:
                        return decisionTablesDetailVo.getIndexValue();
                    default:
                        break;
                }
            }
        }
        return -1;
    }

    private void terminalCondition(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap, Object executeResult) {
        String resultKey = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_terminal_result";
        Map<String, Object> map = new HashMap<>();
        map.put(resultKey, executeResult);
        ExecuteUtils.terminalCondition(engineNode, inputParam, outMap, map);
    }
}
