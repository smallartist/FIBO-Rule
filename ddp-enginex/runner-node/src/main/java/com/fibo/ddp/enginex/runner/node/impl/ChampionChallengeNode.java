package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 冠军挑战节点
 */
@Service
public class ChampionChallengeNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {

    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        logger.info("start【执行冠军挑战节点】ChampionChallengeNode.runNode engineNode:{},inputParam:{},outMap:{}"
                , JSONObject.toJSONString(engineNode), JSONObject.toJSONString(inputParam), JSONObject.toJSONString(outMap));
        if (engineNode != null && engineNode.getSnapshot() != null) {
            outMap.put("nodeSnapshot", engineNode.getSnapshot());
        }
        JSONArray jsonArray = JSONArray.parseArray(engineNode.getNodeJson());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int champion = jsonObject.getIntValue("champion");
            // 返回冠军分支对应的下一个节点
            if(champion == 1){
                outMap.put("nextNode", jsonObject.getString("nextNode"));
                break;
            }
        }
    }

}
