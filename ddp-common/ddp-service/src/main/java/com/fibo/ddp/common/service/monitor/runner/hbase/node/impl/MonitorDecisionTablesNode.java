package com.fibo.ddp.common.service.monitor.runner.hbase.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorStrategy;
import com.fibo.ddp.common.service.monitor.runner.hbase.impl.IMonitorStrategyImpl;
import com.fibo.ddp.common.service.monitor.runner.hbase.node.MonitorService;
import com.fibo.ddp.common.service.strategyx.decisiontable.impl.DecisionTablesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MonitorDecisionTablesNode implements MonitorService {
    private static final Logger logger = LoggerFactory.getLogger(DecisionTablesServiceImpl.class);
    @Autowired
    private IMonitorStrategyImpl iMonitorStrategy;

    @Override
    public void createMonitorStrategy(MonitorNode monitorNode, Map<String, Object> outMap) {
        //根据快照中数据的个数进行确定存取条数。目前决策表只能选择一个，此处数组是预留防止以后多个的情况
        //决策表
        logger.info("MonitorMysqlRuleSetNode============================「监控中心-策略监控信息」参数:{},{}",monitorNode,JSONObject.toJSONString(outMap.get("strategySnopshot")));
        MonitorStrategy.MonitorInfo monitorInfo = new MonitorStrategy.MonitorInfo();
        if(outMap.containsKey("strategySnapshot")){
            String snopshot = outMap.get("strategySnapshot")+"";
            monitorInfo.setSnapshot(JSONObject.parseObject(snopshot));
            outMap.remove("strategySnapshot");
        }
        JSONArray jsonArray = JSONArray.parseArray(monitorInfo.getSnapshot().get("snapshot")+"");
        for (Object object:jsonArray) {
            if(object==null){
                continue;
            }
            MonitorStrategy monitorStrategy1 = new MonitorStrategy();
            //1.节点监控JSONObject信息中拿出策略层面的信息
            MonitorStrategy.MonitorInfo monitorInfo1 = new MonitorStrategy.MonitorInfo();
            JSONObject jsonObject = JSONObject.parseObject(object+"");
            //策略快照
            monitorInfo1.setSnapshot(jsonObject);
            //全量入参
            monitorInfo1.setParams(monitorNode.getMonitorInfo().getParams());
            //决策表结果
            monitorInfo1.setResult(monitorNode.getMonitorInfo().getResult());
            logger.info("MonitorMysqlRuleSetNode============================「监控中心-策略监控信息」monitorInfo:{}",monitorInfo1);
            MonitorStrategy.BaseInfo baseInfo = new MonitorStrategy.BaseInfo();
            //策略ID 即 决策表版本id
            String ruleVersionId = jsonObject.get("id")+"";
            baseInfo.setStrategyId(ruleVersionId);
            //策略名称 决策表名称
            baseInfo.setStrategyName(monitorNode.getMonitorInfo().getSnapshot().getString("name"));
            //策略类型
            baseInfo.setStrategyType(monitorNode.getBaseInfo().getNodeType());
            //业务id
            baseInfo.setBusinessId(monitorNode.getBaseInfo().getBusinessId());
            //节点id
            baseInfo.setNodeId(monitorNode.getBaseInfo().getNodeId());
            //节点类型
            baseInfo.setNodeType(monitorNode.getBaseInfo().getNodeType());
            //引擎版本id
            baseInfo.setEngineVersionId(monitorNode.getBaseInfo().getEngineVersionId());
            logger.info("MonitorMysqlRuleSetNode============================「监控中心-策略监控信息」baseInfo:{}",baseInfo);
            monitorStrategy1.setBaseInfo(baseInfo);
            monitorStrategy1.setMonitorInfo(monitorInfo1);
            //行键生成 节点层面的行键+决策表版本Id
            String rowKeyStr = monitorNode.getRowKey()+ruleVersionId;
            monitorStrategy1.setRowKey(rowKeyStr);
            logger.info("MonitorMysqlRuleSetNode============================「监控中心-策略监控信息」行键:{}",rowKeyStr);
            iMonitorStrategy.putMonitorStrategyToHbase(monitorStrategy1);
        }
    }
}
