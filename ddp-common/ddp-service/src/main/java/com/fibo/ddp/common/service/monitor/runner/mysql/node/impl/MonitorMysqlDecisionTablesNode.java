package com.fibo.ddp.common.service.monitor.runner.mysql.node.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorStrategyMapper;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorStrategy;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTables;
import com.fibo.ddp.common.service.monitor.runner.mysql.node.MonitorMysqlService;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MonitorMysqlDecisionTablesNode implements MonitorMysqlService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorMysqlDecisionTablesNode.class);
    @Autowired
    private TMonitorStrategyMapper monitorStrategyMapper;

    @Autowired
    private DecisionTablesService decisionTablesService;

    @Override
    public void createMonitorStrategy(TMonitorNode monitorNode, Map<String, Object> outMap) {
        //根据快照中数据的个数进行确定存取条数。目前决策表只能选择一个，此处数组是预留防止以后多个的情况
        //决策表
        logger.info("MonitorMysqlDecisionTablesNode============================「监控中心-策略监控信息」参数:{},{}", monitorNode, JSONObject.toJSONString(outMap.get("strategySnopshot")));
        if (!outMap.containsKey("decisionTableStrategy")) {
            return;
        }
        JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(outMap.get("decisionTableStrategy") + "").get("snapshot") + "");
        outMap.remove("decisionTableStrategy");
        for (Object object : jsonArray) {
            if (object == null) {
                continue;
            }
            TMonitorStrategy monitorStrategy1 = new TMonitorStrategy();
            //1.节点监控JSONObject信息中拿出策略层面的信息
            JSONObject jsonObject = JSONObject.parseObject(object + "");
            //策略快照
            monitorStrategy1.setSnapshot(jsonObject.toString());
            //全量入参
            monitorStrategy1.setInput(monitorNode.getInput());
            //执行结果全量输出
            monitorStrategy1.setOutput(monitorNode.getOutput());
            logger.info("MonitorMysqlDecisionTablesNode============================「监控中心-策略监控信息」monitorInfo:{}", monitorStrategy1);
            //策略ID 即 决策表版本id
            String ruleVersionId = jsonObject.get("id") + "";
            String decisionTableId =   jsonObject.getString("decisionTablesId");
            monitorStrategy1.setStrategyId(Long.valueOf(ruleVersionId));
            String result = getResult(monitorNode.getOutput(), decisionTableId);
            //决策表执行结果
            monitorStrategy1.setResult(result);
            //获取对应决策表的名称
            String name = getStrategyName(decisionTableId);
            //策略名称 决策表名称
            monitorStrategy1.setStrategyName(name);
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
            logger.info("MonitorMysqlDecisionTablesNode============================「监控中心-策略监控信息」baseInfo:{}", monitorStrategy1);
            //组织id
            monitorStrategy1.setOrganId(monitorNode.getOrganId());
            //时间
            monitorStrategy1.setCreateTime(LocalDateTime.now());
            monitorStrategy1.setMonitorParentId(monitorNode.getId() + "");
            monitorStrategy1.setEngineId(monitorNode.getEngineId());
            monitorStrategyMapper.insert(monitorStrategy1);
        }
    }

    /**
     * 根据决策表id 查询决策表名称
     * @param decisionTablesId
     * @return
     */
    private String getStrategyName(String decisionTablesId) {
        DecisionTables decisionTables = decisionTablesService.getById(decisionTablesId);
        return decisionTables==null?null:decisionTables.getName();
    }


    /**
     * 根据决策表id 去全量执行结果中匹配
     * 匹配出对应的决策表的 执行结果
     *
     * @param output
     * @param ruleVersionId
     * @return
     */
    private static String getResult(String output, String ruleVersionId) {
        if (!StringUtils.isEmpty(output) && !StringUtils.isEmpty(ruleVersionId)) {
            JSONObject resultJsonObject = JSONObject.parseObject(output);
            JSONArray allResults = JSONArray.parseArray(resultJsonObject.getString("result"));
            List<Object> one = allResults.stream().filter(
                    object -> ruleVersionId.equals(JSONObject.parseObject(JSON.toJSONString(object)).getString("decisionTablesId"))).collect(Collectors.toList());

            if(one.size()==1){
                Object strategyResultJson = one.get(0);
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(strategyResultJson));
                return jsonObject.getString("result");
            }
        }
        return null;
    }
}
