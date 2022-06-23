package com.fibo.ddp.common.service.enginex.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.utils.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EngineNodeJsonUtil {
    // 解析nodeJson
    public final static List<Map> getExecuteListFromNodeJson(EngineNode engineNode) {
        JSONObject nodeJson = JSON.parseObject(engineNode.getNodeJson());
        String strategyStr = null;
        switch (engineNode.getNodeType()) {
//            case 2://规则集
//                strategyStr = JSON.toJSONString(nodeJson.getJSONObject("executeGroup").get("strategyList"));
//                break;
            case 4://评分卡
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("scorecardList"));
                break;
            case 5://名单库
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("listDbList"));
                break;
            case 15://模型
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("modelList"));
                break;
            case 16://决策表
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("decisionTableList"));
                break;
            case 17://决策树
                strategyStr = JSON.toJSONString(nodeJson.getJSONArray("decisionTreeList"));
                break;

        }
        List<Map> maps = JSON.parseArray(strategyStr, Map.class);
        return maps;
    }

    //获取执行用的id列表
    public final static List<Long> getExecuteIdList(EngineNode engineNode, String idKey) {
        List<Map> maps = EngineNodeJsonUtil.getExecuteListFromNodeJson(engineNode);
        List<Long> executeIdList = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            for (Map map : maps) {
                if (map.containsKey(idKey)) {
                    Object o = map.get(idKey);
                    if (o != null){
                        Long id = StringUtil.getStrTolong(String.valueOf(o));
                        if (id!=null&&id!=0){
                            executeIdList.add(id);
                        }
                    }

                }
            }
        }
        return executeIdList;
    }

}
