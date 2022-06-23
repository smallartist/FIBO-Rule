package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 并行节点
 */
@Service
public class ParallelNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {

    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        logger.info("start【执行并行节点】ParallelNode.runNode engineNode:{},inputParam:{},outMap:{}"
                , JSONObject.toJSONString(engineNode), JSONObject.toJSONString(inputParam), JSONObject.toJSONString(outMap));
        if (engineNode != null && engineNode.getSnapshot() != null) {
            outMap.put("nodeSnapshot", engineNode.getSnapshot());
        }

    }
}
