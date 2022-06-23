package com.fibo.ddp.common.service.monitor.runner.mysql.node.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorStrategyMapper;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorStrategy;
import com.fibo.ddp.common.service.monitor.runner.mysql.node.MonitorMysqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class MonitorMysqlBlackOrWhiteNode implements MonitorMysqlService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TMonitorStrategyMapper monitorStrategyMapper;
    @Override
    public void createMonitorStrategy(TMonitorNode monitorNode, Map<String, Object> outMap) {
        //名单库需要继续
        //根据快照中数据的个数进行确定存取条数。
        //决策表
        logger.info("MonitorMysqlBlackOrWhiteNode============================「监控中心-策略监控信息」参数:{},{}",monitorNode, JSONObject.toJSONString(outMap.get("strategySnopshot")));
        if(!outMap.containsKey("strategySnapshot")){
            return;
        }
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(outMap.get("strategySnapshot")));
        outMap.remove("strategySnapshot");
        for (Object object:jsonArray) {
            if(object==null){
                continue;
            }
            TMonitorStrategy monitorStrategy1 = new TMonitorStrategy();
            //1.节点监控JSONObject信息中拿出策略层面的信息
            JSONObject jsonObject = JSONObject.parseObject(object+"");
            //策略快照
            monitorStrategy1.setSnapshot(jsonObject.toString());
            //全量入参
            monitorStrategy1.setInput(monitorNode.getInput());
            //决策表结果
            monitorStrategy1.setOutput(monitorNode.getOutput());
            logger.info("MonitorMysqlBlackOrWhiteNode============================「监控中心-策略监控信息」monitorInfo:{}",monitorStrategy1);
            //策略ID 即 决策表版本id
            String ruleVersionId = jsonObject.get("id")+"";
            monitorStrategy1.setStrategyId(Long.valueOf(ruleVersionId));
            //策略名称 决策表名称
            monitorStrategy1.setStrategyName(JSONObject.parseObject(monitorNode.getSnapshot()).getString("name"));
            //策略类型
            monitorStrategy1.setStrategyType(monitorNode.getNodeType());
            //业务id
            monitorStrategy1.setBusinessId(monitorNode.getBusinessId());
            //节点id
            monitorStrategy1.setNodeId(monitorNode.getNodeId());
            //节点类型
            monitorStrategy1.setNodeType(monitorNode.getNodeType());
            //引擎版本id
            monitorStrategy1.setEngineVersionId(monitorNode.getEngineVersionId());
            logger.info("MonitorMysqlBlackOrWhiteNode============================「监控中心-策略监控信息」baseInfo:{}",monitorStrategy1);
            //组织id
            monitorStrategy1.setOrganId(monitorNode.getOrganId());
            //时间
            monitorStrategy1.setCreateTime(LocalDateTime.now());
            monitorStrategy1.setMonitorParentId(monitorNode.getId()+"");
            monitorStrategy1.setEngineId(monitorNode.getEngineId());
            monitorStrategyMapper.insert(monitorStrategy1);
        }


    }
}
