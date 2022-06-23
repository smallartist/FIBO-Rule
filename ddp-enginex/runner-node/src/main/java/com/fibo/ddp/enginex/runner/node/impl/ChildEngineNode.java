package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.service.common.runner.RunnerSessionManager;
import com.fibo.ddp.common.service.common.runner.SessionData;
import com.fibo.ddp.common.service.enginex.risk.EngineService;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChildEngineNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private EngineApiService engineApiService;
    @Autowired
    public EngineService engineService;

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {

    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        SessionData sessionData = RunnerSessionManager.getSession();
        Long organId = sessionData.getOrganId();
        Integer reqType = sessionData.getReqType();
        Long childEngineId = Long.valueOf(engineNode.getNodeJson());
        Map<String, Object> map = new HashMap<>();

        map.put("fields",inputParam);
        map.put("engineId", childEngineId);
        map.put("organId",organId);
        map.put("reqType",reqType);
//        String result = engineApiService.engineApi(map);
        String result = null;
        String engineResult = "";
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.getString("status").equals("0x0000")&&jsonObject.getString("result")!=null) {
            engineResult = jsonObject.getString("result");
        } else {
            logger.error("子引擎执行失败, childEngineId:{},result:{}", childEngineId, result);
        }

        Engine engineVo = engineService.getEngineById(childEngineId);
        //监控中心--节点信息记录(不需要监控策略层面的监控)
        outMap.put("nodeSnapshot",engineVo);
        JSONObject nodeInfo = new JSONObject();
        nodeInfo.put("engineNode",engineNode);
        nodeInfo.put("nodeId",engineNode.getNodeId());
        nodeInfo.put("nodeName",engineNode.getNodeName());
        nodeInfo.put("nodeType",engineNode.getNodeType());
        outMap.put("nodeInfo",nodeInfo);

        jsonObject.put("nodeId", engineNode.getNodeId());
        jsonObject.put("nodeName", engineNode.getNodeName());
        jsonObject.put("engineId", engineVo.getId());
        jsonObject.put("engineName", engineVo.getName());
        //监控中心====》输出结果写入Hbase
        outMap.put("nodeResult",jsonObject);
        if (outMap.containsKey("childEngineJson")) {
            JSONArray resultJson = (JSONArray) outMap.get("childEngineJson");
            resultJson.add(jsonObject);
        } else {
            JSONArray resultJson = new JSONArray();
            resultJson.add(jsonObject);
            outMap.put("childEngineJson", resultJson);
        }
        String key = engineNode.getNodeType() + "_" + engineNode.getNodeId() + "_" + engineNode.getNodeJson() + "_result";
        inputParam.put(key, engineResult);
    }
}
