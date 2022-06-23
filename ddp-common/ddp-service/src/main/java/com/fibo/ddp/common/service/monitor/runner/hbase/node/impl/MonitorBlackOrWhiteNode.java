package com.fibo.ddp.common.service.monitor.runner.hbase.node.impl;

import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.service.monitor.runner.hbase.impl.IMonitorStrategyImpl;
import com.fibo.ddp.common.service.monitor.runner.hbase.node.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MonitorBlackOrWhiteNode implements MonitorService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IMonitorStrategyImpl iMonitorStrategy;

    @Override
    public void createMonitorStrategy(MonitorNode monitorNode, Map<String, Object> outMap) {
        //名单库需要继续
  /*      logger.info("MonitorMysqlScorecardNode============================「监控中心-策略监控信息」参数:{}",monitorNode);
        MonitorStrategy monitorStrategy = new MonitorStrategy();
        MonitorStrategy.MonitorInfo monitorInfo = new MonitorStrategy.MonitorInfo();
        //策略快照 根绝评分卡id，去查对应的字段。
        if(outMap.containsKey("strategySnopshot")){
            String snopshot = outMap.get("strategySnopshot")+"";
            monitorInfo.setSnapshot(JSONObject.parseObject(snopshot));
            outMap.remove("strategySnopshot");
        }
        JSONArray jsonArray = JSONArray.parseArray(monitorInfo.getSnapshot().get("strategySnopshot")+"");
        for (Object object:jsonArray) {
            JSONObject jsonObject = JSONObject.parseObject(object+"");
            MonitorStrategy monitorStrategy1 = new MonitorStrategy();
            MonitorStrategy.MonitorInfo monitorInfo1 = new MonitorStrategy.MonitorInfo();
            //全量入参
            monitorInfo1.setParams(monitorNode.getMonitorInfo().getParams());
            //策略快照信息（名单库）
            monitorInfo1.setSnapshot(jsonObject);
            //输出结果
            monitorInfo1.setResult(monitorNode.getMonitorInfo().getResult());
            logger.info("MonitorMysqlScorecardNode============================「监控中心-策略监控信息」monitorInfo:{}",monitorInfo);
            MonitorStrategy.BaseInfo baseInfo = new MonitorStrategy.BaseInfo();
            //策略ID 即 名单库Id
            String listDBID = jsonObject.get("id")+"";
            baseInfo.setStrategyId(listDBID);
            //策略名称
            baseInfo.setStrategyName(jsonObject.get("listName")+"");
            //策略类型
            baseInfo.setStrategyType(jsonObject.get("listType")+"");
            //业务id
            baseInfo.setBusinessId(monitorNode.getBaseInfo().getBusinessId());
            //节点id
            baseInfo.setNodeId(monitorNode.getBaseInfo().getNodeId());
            //节点类型
            baseInfo.setNodeType(monitorNode.getBaseInfo().getNodeType());
            //引擎版本id
            baseInfo.setEngineVersionId(monitorNode.getBaseInfo().getEngineVersionId());
            logger.info("MonitorMysqlScorecardNode============================「监控中心-策略监控信息」baseInfo:{}",baseInfo);
            monitorStrategy1.setBaseInfo(baseInfo);
            monitorStrategy1.setMonitorInfo(monitorInfo1);
            //行键生成 节点层面的行键+评分卡Id
            String rowKeyStr = monitorNode.getRowKey()+listDBID;
            monitorStrategy1.setRowKey(rowKeyStr);
            logger.info("MonitorMysqlScorecardNode============================「监控中心-策略监控信息」行键:{}",rowKeyStr);
            iMonitorStrategy.putMonitorStrategyToHbase(monitorStrategy1);
        }*/
    }
}
