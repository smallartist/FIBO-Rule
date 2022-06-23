package com.fibo.ddp.common.service.monitor.runner.mysql.node.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorStrategyMapper;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorStrategy;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleInfo;
import com.fibo.ddp.common.service.monitor.runner.mysql.node.MonitorMysqlService;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleService;
import com.fibo.ddp.common.utils.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MonitorMysqlRuleSetNode implements MonitorMysqlService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TMonitorStrategyMapper monitorStrategyMapper;
    @Autowired
    private RuleService ruleService;

    @Override
    public void createMonitorStrategy(TMonitorNode monitorNode, Map<String, Object> outMap) {
        JSONObject nodeJson = JSONObject.parseObject(monitorNode.getSnapshot());
        //根据快照中数据的个数进行确定存取条数。
        //规则
        logger.info("MonitorMysqlRuleSetNode============================「监控中心-策略监控信息」参数:{},{}",monitorNode,JSONObject.toJSONString(outMap.get("strategySnopshot")));
        if(!outMap.containsKey("strategySnopshot")){
            logger.info("该规则没有对应的快照信息");
            return;
        }
        JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(outMap.get("strategySnopshot")+"").get("snopshot")+"");
        outMap.remove("strategySnopshot");
        if(jsonArray == null){
            return;
        }
        logger.info("================================================ mysql策略 条数: ======================={}",jsonArray.size());
        for (Object object:jsonArray) {
            //1.节点监控JSONObject信息中拿出策略层面的信息
            TMonitorStrategy monitorStrategy = new TMonitorStrategy();
            JSONObject jsonObject = JSONObject.parseObject(object+"");
            if(jsonObject==null){
                return;
            }
            //策略快照
            monitorStrategy.setSnapshot(jsonObject.toString());
            //全量入参
            monitorStrategy.setInput(monitorNode.getInput());
            //详细得分情况
            JSONObject scoreDetail = new JSONObject();
            scoreDetail.put("ruleResultList", JSON.parseObject(monitorNode.getOutput()).get("ruleResultList"));
            //策略ID 即 规则版本id
            String ruleVersionId = jsonObject.get("id")+"";
            //输出匹配过滤
//            RuleInfo ruleInfo = matchOutPut(ruleVersionId,ruleVersionId);
            monitorStrategy.setOutput(scoreDetail.toJSONString());
            logger.info("MonitorMysqlRuleSetNode============================「监控中心-策略监控信息」monitorInfo:{}",monitorStrategy);
            RuleInfo ruleInfo = getRuleInfo(nodeJson,ruleVersionId);
            monitorStrategy.setStrategyId(Long.valueOf(ruleVersionId));
            //策略名称 规则版本描述
            monitorStrategy.setStrategyName(ruleInfo.getName());
            //策略类型
            monitorStrategy.setStrategyType(monitorNode.getNodeType());
            //业务id
            monitorStrategy.setBusinessId(monitorNode.getBusinessId());
            //节点id
            monitorStrategy.setNodeId(monitorNode.getNodeId());
            //节点类型
            monitorStrategy.setNodeType(monitorNode.getNodeType());
            //引擎版本id
            monitorStrategy.setEngineVersionId(monitorNode.getEngineVersionId());
            monitorStrategy.setMonitorParentId(monitorNode.getId()+"");
            monitorStrategy.setEngineId(monitorNode.getOrganId());
            monitorStrategy.setOrganId(monitorNode.getOrganId());
            logger.info("MonitorMysqlRuleSetNode============================「监控中心-策略监控信息」baseInfo:{}",monitorStrategy);
            monitorStrategyMapper.insert(monitorStrategy);
        }
    }

    /**
     * 匹配获取规则
     * @param nodeJson
     * @param ruleVersionId
     * @return
     */
    private RuleInfo getRuleInfo(JSONObject nodeJson, String ruleVersionId) {
        int groupType = nodeJson.getInteger("groupType") == null ? Constants.ruleNode.EXECUTEGROUP : nodeJson.getInteger("groupType");
        RuleInfo ruleInfo = new RuleInfo();
        // 互斥组(串行)
        if (groupType == Constants.ruleNode.MUTEXGROUP) {
            JSONArray jsonArray1 = nodeJson.getJSONObject("mutexGroup").getJSONArray("rules");
            ruleInfo = getRuleFromJsonArray(jsonArray1,ruleVersionId);
        }
        // 执行组(并行)
        else if (groupType == Constants.ruleNode.EXECUTEGROUP) {
            JSONArray jsonArray1 = nodeJson.getJSONObject("executeGroup").getJSONArray("rules");
            ruleInfo = getRuleFromJsonArray(jsonArray1,ruleVersionId);
        }
        return ruleInfo;
    }

    private RuleInfo getRuleFromJsonArray(JSONArray jsonArray,String ruleVersionId) {
        List<Long> ruleIds = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject ruleObj = jsonArray.getJSONObject(i);
            Long versionId = ruleObj.getLong("ruleVersionId");
            if(versionId.longValue()==Long.valueOf(ruleVersionId).longValue()){
                Long ruleId = ruleObj.getLong("id");
                ruleIds.add(ruleId);
            }
        }
        List<RuleInfo> ruleInfoList = ruleService.getRuleList(ruleIds);
        return ruleInfoList.get(0);
    }
}
