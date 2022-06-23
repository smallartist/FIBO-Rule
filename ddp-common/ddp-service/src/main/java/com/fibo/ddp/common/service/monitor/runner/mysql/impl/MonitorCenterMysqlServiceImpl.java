package com.fibo.ddp.common.service.monitor.runner.mysql.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorEngineMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorNodeMapper;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorEngine;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;
import com.fibo.ddp.common.service.monitor.runner.IMonitorCenterService;
import com.fibo.ddp.common.service.monitor.runner.mysql.node.impl.*;
import com.fibo.ddp.common.utils.constant.monitor.MonitorStorageType;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 决策流监控 mysql存储实现类
 */
@Service
public class MonitorCenterMysqlServiceImpl implements IMonitorCenterService {

    private static final Logger logger = LoggerFactory.getLogger(MonitorCenterMysqlServiceImpl.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private TMonitorEngineMapper monitorEngineMapper;
    @Autowired
    private TMonitorNodeMapper monitorNodeMapper;
    @Autowired
    private MonitorMysqlRuleSetNode monitorMysqlRuleSetNode;
    @Autowired
    private MonitorMysqlScorecardNode monitorMysqlScorecardNode;
    @Autowired
    private MonitorMysqlDecisionTablesNode monitorMysqlDecisionTablesNode;
    @Autowired
    private MonitorMysqlBlackOrWhiteNode monitorMysqlBlackOrWhiteNode;
    @Autowired
    private MonitorMysqlDecisionTreeNode monitorMysqlDecisionTreeNode;

    @Override
    public String getStorageType() {
        return MonitorStorageType.Mysql;
    }

    @Override
    public void monitorDecisionFlow(Map<String, Object> inputParam, Engine engine, EngineVersion engineVersion, List<EngineNode> engineNodeList, Map<String, Object> outMap, Map<String, Object> paramJson, Integer resultId) {
        CompletableFuture.runAsync(()->{
            try {
                //组装插入实体类信息
                TMonitorEngine monitorEngine = new TMonitorEngine();
                monitorEngine.setBusinessId((String) paramJson.get("pid"));
                monitorEngine.setMonitorParentId(resultId+"");
                monitorEngine.setOrganId(engine.getOrganId());
                monitorEngine.setEngineId(engine.getId());
                monitorEngine.setEngineName(engine.getName());
                monitorEngine.setEngineVersionId(engineVersion.getVersionId());
                monitorEngine.setProcess(outMap.get("executedNodes")+"");
                monitorEngine.setSnapshot(JSON.toJSONString(engineNodeList));
                monitorEngine.setInput(JSON.toJSONString(inputParam));
                //todo 异步写入mysql
                monitorEngineMapper.insert(monitorEngine);
                outMap.remove("executedNodes");
                //todo 组装节点监控 mysql节点业务模型 并存入mysql
                monitorNode(engine,engineVersion,outMap,monitorEngine);
            } catch (Exception e) {
                logger.info("==============监控中心========监控信息入库出错:{}",e);
            }
        },threadPoolTaskExecutor);
    }


    /**
     * 组装节点监控 Hbase节点业务模型 并存入hbase
     * @param engine
     * @param engineVersion
     * @param outMap
     * @param monitorEngine
     */
    private void monitorNode(Engine engine, EngineVersion engineVersion, Map<String, Object> outMap,TMonitorEngine monitorEngine) {
        if(outMap.containsKey("monitorNodes")){
            List<MonitorNode> monitorNodeInfoList = (List<MonitorNode>)outMap.get("monitorNodes");
            logger.debug("=============================monitorNode:{}", JSONArray.toJSONString(monitorNodeInfoList));
            //循环拿出list中的数据，重新组装业务模型，插入Hbase表中
            monitorNodeInfoList.stream().forEach(item->{
                //组装节点层面 插入实体类信息
                TMonitorNode monitorNode = new TMonitorNode();
                monitorNode.setBusinessId(item.getBaseInfo().getBusinessId());
                monitorNode.setOrganId(engine.getOrganId());
                monitorNode.setEngineId(engine.getId());
                monitorNode.setEngineVersionId(engineVersion.getVersionId());
                monitorNode.setMonitorParentId(monitorEngine.getId()+"");
                monitorNode.setNodeId(Long.valueOf(item.getBaseInfo().getNodeId()));
                monitorNode.setNodeName(item.getBaseInfo().getNodeName());
                monitorNode.setNodeType(item.getBaseInfo().getNodeType());
                monitorNode.setSnapshot(JSON.toJSONString(item.getMonitorInfo().getSnapshot()));
                monitorNode.setInput(JSON.toJSONString(item.getMonitorInfo().getParams()));
                monitorNode.setOutput(JSON.toJSONString(item.getMonitorInfo().getResult()));
                monitorNode.setCreateTime(LocalDateTime.now());
                monitorNodeMapper.insert(monitorNode);
                //根绝节点类型拆分出策略层面的数据，组装策略监控信息业务模型
                buildMonitorStrategyModel(monitorNode,outMap);
            });
            outMap.remove("monitorDecisionFlow");
        }
    }
    /**
     * 根绝节点类型拆分出策略层面的数据，组装策略监控信息业务模型
     * 并写入Hbase
     * @param monitorNode
     * @Param outMap 输出变量池
     */
    public void buildMonitorStrategyModel(TMonitorNode monitorNode, Map<String, Object> outMap) {
        String nodeType = monitorNode.getNodeType();
        switch(Integer.valueOf(StringEscapeUtils.unescapeJava(nodeType))){
            case 2:
                //规则
                monitorMysqlRuleSetNode.createMonitorStrategy(monitorNode,outMap);
                break;
            case 3:
                //分组
                break;
            case 4:
                //评分卡
                monitorMysqlScorecardNode.createMonitorStrategy(monitorNode,outMap);
                break;
            case 5:
                //黑名单
                monitorMysqlBlackOrWhiteNode.createMonitorStrategy(monitorNode,outMap);
                break;
            case 6:
                //白名单
                monitorMysqlBlackOrWhiteNode.createMonitorStrategy(monitorNode,outMap);
                break;
            case 7:
                //沙盒比例
                break;
            case 9:
                //决策选项
                break;
            case 14:
                //子引擎
                break;
            case 15:
                //模型
                break;
            case 16:
                //决策表
                monitorMysqlDecisionTablesNode.createMonitorStrategy(monitorNode,outMap);
                break;
            case 17:
                //决策树
                monitorMysqlDecisionTreeNode.createMonitorStrategy(monitorNode,outMap);
                break;
            default:
                break;
        }
    }
}
