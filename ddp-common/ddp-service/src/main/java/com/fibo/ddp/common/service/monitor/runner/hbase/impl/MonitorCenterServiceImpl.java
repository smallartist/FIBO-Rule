package com.fibo.ddp.common.service.monitor.runner.hbase.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fibo.ddp.common.dao.enginex.risk.EngineResultSetMapper;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorDecisionFlow;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.service.monitor.runner.IMonitorCenterService;
import com.fibo.ddp.common.service.monitor.runner.hbase.node.impl.MonitorBlackOrWhiteNode;
import com.fibo.ddp.common.service.monitor.runner.hbase.node.impl.MonitorDecisionTablesNode;
import com.fibo.ddp.common.service.monitor.runner.hbase.node.impl.MonitorRuleSetNode;
import com.fibo.ddp.common.service.monitor.runner.hbase.node.impl.MonitorScorecardNode;
import com.fibo.ddp.common.utils.constant.monitor.MonitorStorageType;
import com.fibo.ddp.common.utils.constant.monitor.RowKeyUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service("monitorCenterServiceImpl")
public class MonitorCenterServiceImpl implements IMonitorCenterService {

    private static final Logger logger = LoggerFactory.getLogger(MonitorCenterServiceImpl.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private IMonitorDecisionFlowImpl iMonitorDecisionFlow;
    @Autowired
    private IMonitorNodeImpl iMonitorNode;
    @Autowired
    private MonitorScorecardNode monitorScorecardNode;
    @Autowired
    private MonitorRuleSetNode monitorRuleSetNode;
    @Autowired
    private MonitorBlackOrWhiteNode monitorBlackOrWhiteNode;
    @Autowired
    private MonitorDecisionTablesNode monitorDecisionTablesNode;
    @Resource
    public EngineResultSetMapper resultSetMapper;


    @Override
    public String getStorageType() {
        return MonitorStorageType.HBase;
    }

    @Override
    public void monitorDecisionFlow(Map<String, Object> inputParam, Engine engine, EngineVersion engineVersion, List<EngineNode> engineNodeList, Map<String, Object> outMap, Map<String, Object> paramJson, Integer resultId) {
        CompletableFuture.runAsync(()->{
            try {
                MonitorDecisionFlow.MonitorInfo monitorInfo = new MonitorDecisionFlow.MonitorInfo(
                        inputParam,
                        engineNodeList,
                        (List<String>)outMap.get("executedNodes"));
                MonitorDecisionFlow.BaseInfo baseInfo = new MonitorDecisionFlow.BaseInfo(
                        (String) paramJson.get("pid"),
                        engine.getName(),
                        engineVersion.getVersionId()+"",
                        engineVersion);
                //生成行键
                String rowKeyStr = RowKeyUtil.formatLastUpdate(new Date().getTime());
                //传递到节点监控。用于节点监控行键生成
                outMap.put("monitorDecisionFlow",rowKeyStr);
                MonitorDecisionFlow monitorDecisionFlow = new MonitorDecisionFlow(rowKeyStr,monitorInfo,baseInfo);
                logger.info("=================================决策流监控:{}", JSON.toJSONString(monitorDecisionFlow));
                //todo 异步写入Hbase
                iMonitorDecisionFlow.putMonitorDecisionFlowToHbase(monitorDecisionFlow);
                updateResultSet(resultId,rowKeyStr);
                outMap.remove("executedNodes");
                //todo 组装节点监控 Hbase节点业务模型 并存入hbase
                monitorNode(engine,engineVersion,outMap);
            } catch (Exception e) {
                logger.info("==============监控中心========监控信息入库出错:{}",e);
            }
        },threadPoolTaskExecutor);
    }
    /**
     *  反写t_resultset表中hbase_row_key字段
     * @param resultId
     * @param rowKeyStr
     */
    private void updateResultSet(Integer resultId, String rowKeyStr) {
        resultSetMapper.updateResultById(resultId,rowKeyStr);
    }

    /**
     * 组装节点监控 Hbase节点业务模型 并存入hbase
     * @param engine
     * @param engineVersion
     * @param outMap
     */
    private void monitorNode(Engine engine, EngineVersion engineVersion, Map<String, Object> outMap) {
        if(outMap.containsKey("monitorNodes")){
            List<MonitorNode> monitorNodeInfoList = (List<MonitorNode>)outMap.get("monitorNodes");
            logger.info("=============================monitorNode:{}", JSONArray.toJSONString(monitorNodeInfoList));
            //循环拿出list中的数据，重新组装业务模型，插入Hbase表中
            monitorNodeInfoList.stream().forEach(item->{
                MonitorNode monitorNode = new MonitorNode();
                //监控信息组装
                MonitorNode.MonitorInfo monitorInfo = new MonitorNode.MonitorInfo();
                monitorInfo.setParams(item.getMonitorInfo().getParams());
                monitorInfo.setResult(item.getMonitorInfo().getResult());
                monitorInfo.setSnapshot(item.getMonitorInfo().getSnapshot());
                //基本信息组装
                MonitorNode.BaseInfo baseInfo = new MonitorNode.BaseInfo();
                baseInfo.setEngineVersionId(engineVersion.getVersionId()+"");
                baseInfo.setNodeId(item.getBaseInfo().getNodeId());
                baseInfo.setNodeName(item.getBaseInfo().getNodeName());
                baseInfo.setNodeType(item.getBaseInfo().getNodeType());
                baseInfo.setNodeInfo(item.getBaseInfo().getNodeInfo());
                baseInfo.setEngineInfo(engineVersion);
                monitorNode.setMonitorInfo(monitorInfo);
                monitorNode.setBaseInfo(baseInfo);
                //todo 获取行键 ，行键生成策略 hkey+nodeId
                if(outMap.containsKey("monitorDecisionFlow")){
                    String rowKey = outMap.get("monitorDecisionFlow")+"";
                    monitorNode.setRowKey(rowKey+item.getBaseInfo().getNodeId());
                }
                //根绝节点类型拆分出策略层面的数据，组装策略监控信息业务模型
                buildMonitorStrategyModel(monitorNode,outMap);
                iMonitorNode.putMonitorNodeToHbase(monitorNode);
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
    public void buildMonitorStrategyModel(MonitorNode monitorNode, Map<String, Object> outMap) {
        String nodeType = monitorNode.getBaseInfo().getNodeType();
        switch(Integer.valueOf(StringEscapeUtils.unescapeJava(nodeType))){
            case 2:
                //规则
                monitorRuleSetNode.createMonitorStrategy(monitorNode,outMap);
                break;
            case 3:
                //分组
                break;
            case 4:
                //评分卡
                monitorScorecardNode.createMonitorStrategy(monitorNode,outMap);
                break;
            case 5:
                //黑名单
                monitorBlackOrWhiteNode.createMonitorStrategy(monitorNode,outMap);
                break;
            case 6:
                //白名单
                monitorBlackOrWhiteNode.createMonitorStrategy(monitorNode,outMap);
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
                monitorDecisionTablesNode.createMonitorStrategy(monitorNode,outMap);
                break;
            default:
                break;
        }
    }

}
