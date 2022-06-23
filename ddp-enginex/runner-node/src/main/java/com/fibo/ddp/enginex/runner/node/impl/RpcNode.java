package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.datax.datainterface.InterfaceInfo;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.service.datax.datainterface.InterfaceService;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 远程调用节点
 */
@Service
public class RpcNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InterfaceService interfaceService;

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {

    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        logger.info("start【执行RPC节点】RpcNode.runNode engineNode:{},inputParam:{},outMap:{}"
                , JSONObject.toJSONString(engineNode), JSONObject.toJSONString(inputParam), JSONObject.toJSONString(outMap));
        if (engineNode != null && engineNode.getSnapshot() != null) {
            outMap.put("nodeSnapshot", engineNode.getSnapshot());
        }
        JSONObject nodeJson = JSONObject.parseObject(engineNode.getNodeJson());
        JSONObject callConfig = nodeJson.getJSONObject("callConfig");
        int callType = callConfig.getInteger("callType");
        JSONArray resultConfig = nodeJson.getJSONArray("resultConfig");
        InterfaceInfo interfaceInfo = interfaceService.getInterfaceById(callConfig.getInteger("interfaceId"));
        // 发送http请求
        String result = interfaceService.getHttpResponse(interfaceInfo, inputParam, callType);
        // 解析指标
        parseField(result, resultConfig, inputParam);
    }

    private void parseField(String result, JSONArray resultConfig, Map<String, Object> inputParam){
        if(resultConfig != null && !resultConfig.isEmpty() && StringUtils.isNotBlank(result)){
            JSONObject configJson = resultConfig.getJSONObject(0);
            String fieldEn = configJson.getString("fieldEn");
            String parseField = configJson.getString("parseField");
            String fieldValue = interfaceService.interfaceParseField(parseField, result);
            inputParam.put(fieldEn, fieldValue);
        }
    }
}
