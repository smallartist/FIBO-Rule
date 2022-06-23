package com.fibo.ddp.common.service.monitor.runner.hbase.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorStrategy;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVo;
import com.fibo.ddp.common.service.monitor.runner.hbase.impl.IMonitorStrategyImpl;
import com.fibo.ddp.common.service.monitor.runner.hbase.node.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MonitorScorecardNode implements MonitorService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IMonitorStrategyImpl iMonitorStrategy;

    @Override
    public void createMonitorStrategy(MonitorNode monitorNode, Map<String, Object> outMap) {
        logger.info("MonitorMysqlScorecardNode============================「监控中心-策略监控信息」参数:{}",monitorNode);
        MonitorStrategy monitorStrategy = new MonitorStrategy();
        MonitorStrategy.MonitorInfo monitorInfo = new MonitorStrategy.MonitorInfo();
        //策略快照 根绝评分卡id，去查对应的字段。
        if(outMap.containsKey("strategySnopshot")){
            String snopshot = outMap.get("strategySnopshot")+"";
            monitorInfo.setSnapshot(JSONObject.parseObject(snopshot));
            outMap.remove("strategySnopshot");
        }
        ScorecardVo scorecardVo = JSONObject.parseObject(JSONObject.toJSONString(monitorInfo.getSnapshot()),ScorecardVo.class);
        if(scorecardVo==null){
            return;
        }
//        monitorInfo.setSnapshot(JSONObject.parseObject(JSONObject.toJSONString(scorecardVo.getVersionList().get(0))));
        //全量入参
        monitorInfo.setParams(monitorNode.getMonitorInfo().getParams());
        //详细得分情况
        JSONObject scoreDetail = new JSONObject();
        scoreDetail.put("scoreDetail",monitorNode.getMonitorInfo().getResult().get("scoreDetail"));
        monitorInfo.setResult(scoreDetail);
        logger.info("MonitorMysqlScorecardNode============================「监控中心-策略监控信息」monitorInfo:{}",monitorInfo);
        MonitorStrategy.BaseInfo baseInfo = new MonitorStrategy.BaseInfo();
        //策略ID 即 评分卡版本Id
        baseInfo.setStrategyId(scorecardVo.getId()+"");
        //策略名称
        baseInfo.setStrategyName(scorecardVo.getName()+"");
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
        logger.info("MonitorMysqlScorecardNode============================「监控中心-策略监控信息」baseInfo:{}",baseInfo);
        monitorStrategy.setBaseInfo(baseInfo);
        monitorStrategy.setMonitorInfo(monitorInfo);
        //行键生成 节点层面的行键+评分卡Id
        String rowKeyStr = monitorNode.getRowKey()+scorecardVo.getId();
        monitorStrategy.setRowKey(rowKeyStr);
        logger.info("MonitorMysqlScorecardNode============================「监控中心-策略监控信息」行键:{}",rowKeyStr);
        iMonitorStrategy.putMonitorStrategyToHbase(monitorStrategy);
    }
}
