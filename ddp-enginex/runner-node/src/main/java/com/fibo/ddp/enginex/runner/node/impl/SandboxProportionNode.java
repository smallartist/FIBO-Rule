package com.fibo.ddp.enginex.runner.node.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.runner.Sandbox;
import com.fibo.ddp.enginex.runner.node.EngineRunnerNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class SandboxProportionNode implements EngineRunnerNode {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void getNodeField(EngineNode engineNode, Map<String, Object> inputParam) {

    }

    @Override
    public void runNode(EngineNode engineNode, Map<String, Object> inputParam, Map<String, Object> outMap) {
        if (null != engineNode.getNodeScript()) {
            List<Sandbox> list = JSON.parseArray(engineNode.getNodeScript(), Sandbox.class);
            //监控中心-- 节点配置信息记录(不需要策略层面的监控)
            JSONObject nodeSnapshot = new JSONObject();
            nodeSnapshot.put("nodeSnapshot",JSON.parseArray(engineNode.getNodeJson()));
            outMap.put("nodeSnapshot",nodeSnapshot);
            JSONObject nodeInfo = new JSONObject();
            nodeInfo.put("engineNode",engineNode);
            nodeInfo.put("nodeId",engineNode.getNodeId());
            nodeInfo.put("nodeName",engineNode.getNodeName());
            nodeInfo.put("nodeType",engineNode.getNodeType());
            outMap.put("nodeInfo",nodeInfo);
            int num = 0;//随机生成的数
            int startNum = 0;
            int endNum = 0;
            for (int i = 0; i < list.size(); i++) {
                Sandbox sandbox = list.get(i);
                endNum = startNum + sandbox.getProportion();
                if (num == 0)
                    num = getRandoms(0, sandbox.getSum(), 1)[0];
                int[] range = getRandoms(startNum, endNum, sandbox.getProportion());
                for (int j = 0; j < range.length; j++) {
                    if (range[j] == num) {
                        if (StringUtils.isBlank(sandbox.getNextNode())) {
                            List<Sandbox> sblist = JSON.parseArray(engineNode.getNodeJson(), Sandbox.class);
                            for (Sandbox sb : sblist) {
                                if (sb.getSandbox() == sandbox.getSandbox()) {
                                    sandbox.setNextNode(sb.getNextNode());
                                    break;
                                }
                            }
                        }

                        outMap.put("nextNode", sandbox.getNextNode());
                        JSONObject nodeResult = new JSONObject();
                        nodeResult.put("nodeResult",sandbox.getNextNode());
                        outMap.put("nodeResult",nodeResult);
                        break;
                    }
                }
                startNum = endNum;
            }
        }
    }

    /**
     * 根据min和max随机生成count个不重复的随机数组
     *
     * @param min
     * @param max
     * @param count
     * @return int[]
     */
    public int[] getRandoms(int min, int max, int count) {
        int[] randoms = new int[count];
        List<Integer> listRandom = new ArrayList<Integer>();

        if (count > (max - min + 1)) {
            return null;
        }
        // 将所有的可能出现的数字放进候选list
        for (int i = min; i < max; i++) {
            listRandom.add(i);
        }
        // 从候选list中取出放入数组，已经被选中的就从这个list中移除
        for (int i = 0; i < count; i++) {
            int index = getRandom(0, listRandom.size() - 1);
            randoms[i] = listRandom.get(index);
            listRandom.remove(index);
        }

        return randoms;
    }

    /**
     * 根据min和max随机生成一个范围在[min,max]的随机数，包括min和max
     *
     * @param min
     * @param max
     * @return int
     */
    public int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
