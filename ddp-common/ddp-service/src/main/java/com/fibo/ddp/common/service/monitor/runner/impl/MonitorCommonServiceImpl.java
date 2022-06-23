package com.fibo.ddp.common.service.monitor.runner.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.service.monitor.runner.MonitorCommonService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MonitorCommonServiceImpl implements MonitorCommonService {

    private static final Logger logger = LoggerFactory.getLogger(MonitorCommonServiceImpl.class);

    /**
     * 组装该节点监控信息部分数据
     * @param inputParam
     * @param engineNode
     * @param outMap
     * @param monitorNodeInfoList
     */
    @Override
    public void buildMonitorNode(Map<String, Object> inputParam, EngineNode engineNode, Map<String, Object> outMap, List<MonitorNode> monitorNodeInfoList) {
        try {
            MonitorNode.MonitorInfo monitorInfo = new MonitorNode.MonitorInfo();
            MonitorNode.BaseInfo baseInfo = new MonitorNode.BaseInfo();
            //输出变量池 节点快照(节点配置，不同节点的配置获取情况不同，一般直接拿nodeJson)
            if(outMap.containsKey("nodeSnapshot")){
                JSONObject snapshot= null;
                try {
                    snapshot = JSONObject.parseObject(StringEscapeUtils.unescapeJava(JSON.toJSONString(outMap.get("nodeSnapshot"))));
                } catch (Exception e) {
                    snapshot = JSON.parseObject(outMap.get("nodeSnapshot")+"");
                }
                monitorInfo.setSnapshot(snapshot);
                outMap.remove("nodeSnapshot");
            }
            //输出变量池 获取节点输出结果(不同节点类型的输出结果不同)
            if(outMap.containsKey("nodeResult")){
                JSONObject result=JSONObject.parseObject(StringEscapeUtils.unescapeJava(JSON.toJSONString(outMap.get("nodeResult"))));
                monitorInfo.setResult(result);
                outMap.remove("nodeResult");
            }
            //输出变量池 获取节点基本信息(节点执行的实现方法中添加）
            if(outMap.containsKey("nodeInfo")){
                JSONObject nodeInfo=(JSONObject) outMap.get("nodeInfo");
                baseInfo.setNodeInfo(JSON.toJSONString(nodeInfo.get("engineNode")));
                baseInfo.setNodeId(JSON.toJSONString(nodeInfo.get("nodeId")));
                baseInfo.setNodeType(JSON.toJSONString(nodeInfo.get("nodeType")));
                baseInfo.setNodeName(JSON.toJSONString(nodeInfo.get("nodeName")));
                outMap.remove("nodeInfo");
            }
            monitorInfo.setParams(inputParam);
            MonitorNode monitorNode = new MonitorNode();
            monitorNode.setMonitorInfo(monitorInfo);
            monitorNode.setBaseInfo(baseInfo);
            monitorNodeInfoList.add(monitorNode);
        } catch (Exception e) {
            logger.info("============================监控中心===组装该节点监控信息部分数据=====:{}",e);
        }
    }
}
