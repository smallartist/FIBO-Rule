package com.fibo.ddp.common.service.analyse.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fibo.ddp.common.dao.enginex.risk.EngineMapper;
import com.fibo.ddp.common.dao.enginex.risk.EngineVersionMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorEngineMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorNodeMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorStrategyMapper;
import com.fibo.ddp.common.model.analyse.*;
import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.model.enginex.risk.EngineResultSetDTO;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorEngine;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNodeDTO;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorStrategyDTO;
import com.fibo.ddp.common.service.analyse.*;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by niuge on 2021/11/5.
 */
@Service
public class AnalyseChartStatisticServiceImpl implements StatisticsService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EngineVersionMapper engineVersionMapper;
    @Autowired
    private EngineMapper engineMapper;
    @Autowired
    private AnalyseEngineCallService analyseEngineCallService;
    @Autowired
    private TMonitorEngineMapper monitorEngineMapper;
    @Autowired
    private TMonitorNodeMapper monitorNodeMapper;
    @Autowired
    private TMonitorStrategyMapper monitorStrategyMapper;
    @Autowired
    private AnalyseDecisionResultService analyseDecisionResultService;
    @Autowired
    private AnalyseRuleService analyseRuleService;
    @Autowired
    private AnalyseScorecardService analyseScorecardService;
    @Autowired
    private AnalyseDecisionTablesService analyseDecisionTablesService;
    @Autowired
    private AnalyseEngineNodeService analyseEngineNodeService;

    @Override
    public void statisticData() {
        //图表数据统计
        AnalyseChartEngineCallCount();
        //决策引擎执行结果 统计
        AnalyseDecisionResultCount();
        //决策引擎规则命中次数 统计
        AnalyseRuleCount();
        //决策引擎规则评分卡命中次数 统计
        AnalyseScorecardCount();
        //决策引擎 决策表命中次数 统计
        AnalyseDecisionTablesCount();
        //决策引擎 节点层面
        AnalyseNodeCount();
    }

    /**
     * 表格 节点命中次数 统计(一天统计 一次)
     */
    private void AnalyseNodeCount() {
        //节点命中次数 统计(一天统计 一次)
        List<TMonitorNodeDTO> monitorNodeDTOS = monitorNodeMapper.countNodeHit();
        List<AnalyseEngineNode> analyseEngineNodes = new ArrayList<>();
        for (int i = 0; i < monitorNodeDTOS.size(); i++) {
            TMonitorNodeDTO tMonitorNodeDTO =  monitorNodeDTOS.get(i);
            buildNode(tMonitorNodeDTO,analyseEngineNodes);
        }
        analyseEngineNodeService.saveBatch(analyseEngineNodes);
    }

    private void buildNode(TMonitorNodeDTO tMonitorNodeDTO, List<AnalyseEngineNode> analyseEngineNodes) {
        AnalyseEngineNode analyseEngineNode = new AnalyseEngineNode();
        analyseEngineNode.setCallDate(tMonitorNodeDTO.getCallDate());
        //引擎id
        analyseEngineNode.setEngineId(tMonitorNodeDTO.getEngineId());
        //引擎版本id
        analyseEngineNode.setVersionId(tMonitorNodeDTO.getEngineVersionId());
        analyseEngineNode.setVersionCode(null);
        analyseEngineNode.setEngineName(null);
        analyseEngineNode.setEngineDescription(null);
        analyseEngineNode.setOrganId(tMonitorNodeDTO.getOrganId());
        analyseEngineNode.setNodeName(tMonitorNodeDTO.getNodeName());
        analyseEngineNode.setNodeId(tMonitorNodeDTO.getNodeId());
        analyseEngineNode.setPassCount(tMonitorNodeDTO.getTotal());
        analyseEngineNode.setCreateTime(new Date());
        analyseEngineNode.setCreateUserId(Long.valueOf(1));
        analyseEngineNode.setUpdateUserId(Long.valueOf(1));
        analyseEngineNode.setUpdateTime(new Date());
        analyseEngineNodes.add(analyseEngineNode);
    }

    /**
     * 表格 决策表调用次数 统计(一天统计 一次)
     */
    private void  AnalyseDecisionTablesCount() {
        List<TMonitorStrategyDTO>  monitorStrategyDTOS = monitorStrategyMapper.countDecisionTables();
        List<AnalyseDecisionTables> analyseDecisionTables = new ArrayList<>();
        for (int i = 0; i < monitorStrategyDTOS.size(); i++) {
            TMonitorStrategyDTO tMonitorStrategyDTO =  monitorStrategyDTOS.get(i);
            buildDecisionTables(tMonitorStrategyDTO,analyseDecisionTables);
        }
        analyseDecisionTablesService.saveBatch(analyseDecisionTables);
    }

    private void buildDecisionTables(TMonitorStrategyDTO tMonitorStrategyDTO, List<AnalyseDecisionTables> analyseDecisionTables) {
        AnalyseDecisionTables decisionTables = new AnalyseDecisionTables();
        decisionTables.setCallDate(tMonitorStrategyDTO.getCallDate());
        //引擎id
        decisionTables.setEngineId(tMonitorStrategyDTO.getEngineId());
        decisionTables.setEngineName(null);
        decisionTables.setEngineDescription(null);
        //引擎版本id
        decisionTables.setVersionId(tMonitorStrategyDTO.getEngineVersionId());
        //组织id
        decisionTables.setOrganId(tMonitorStrategyDTO.getOrganId());
        decisionTables.setDecisonTablesId(tMonitorStrategyDTO.getStrategyId());
        decisionTables.setDecisonTablesName(tMonitorStrategyDTO.getStrategyName());
        decisionTables.setDecisonTablesVersionCode(null);
        decisionTables.setDecisonTablesVersionId(null);
        decisionTables.setResultCount(tMonitorStrategyDTO.getTotal());
        //执行结果
        decisionTables.setResult(tMonitorStrategyDTO.getResult());

        decisionTables.setCreateTime(new Date());
        decisionTables.setCreateUserId(Long.valueOf(1));
        decisionTables.setUpdateUserId(Long.valueOf(1));
        decisionTables.setUpdateTime(new Date());
        analyseDecisionTables.add(decisionTables);
    }

    /**
     * 表格 评分卡调用次数 统计(一天统计 一次)
     */
    private void AnalyseScorecardCount() {
       List<TMonitorStrategyDTO>  monitorStrategyDTOS = monitorStrategyMapper.countScorecardHit();
        List<AnalyseScorecard> analyseScorecards = new ArrayList<>();
        for (int i = 0; i < monitorStrategyDTOS.size(); i++) {
            TMonitorStrategyDTO tMonitorStrategyDTO =  monitorStrategyDTOS.get(i);
            buildScorecard(tMonitorStrategyDTO,analyseScorecards);
        }
        analyseScorecardService.saveBatch(analyseScorecards);
    }

    private void buildScorecard(TMonitorStrategyDTO tMonitorStrategyDTO, List<AnalyseScorecard> analyseScorecards) {
        AnalyseScorecard analyseScorecard = new AnalyseScorecard();
        analyseScorecard.setCallDate(tMonitorStrategyDTO.getCallDate());
        //引擎id
        analyseScorecard.setEngineId(tMonitorStrategyDTO.getEngineId());
        analyseScorecard.setEngineName(null);
        analyseScorecard.setEngineDescription(null);
        //引擎版本id
        analyseScorecard.setVersionId(tMonitorStrategyDTO.getEngineVersionId());
        analyseScorecard.setOrganId(tMonitorStrategyDTO.getOrganId());
        analyseScorecard.setScorecardId(tMonitorStrategyDTO.getStrategyId());
        analyseScorecard.setScorecardName(tMonitorStrategyDTO.getStrategyName());
        analyseScorecard.setScorecardVersionCode(null);
        analyseScorecard.setScorecardVersionId(null);
        //评分卡执行输出结果
        analyseScorecard.setResult(tMonitorStrategyDTO.getResult());
        analyseScorecard.setResultCount(tMonitorStrategyDTO.getTotal());
        analyseScorecard.setCreateTime(new Date());
        analyseScorecard.setCreateUserId(Long.valueOf(1));
        analyseScorecard.setUpdateUserId(Long.valueOf(1));
        analyseScorecard.setUpdateTime(new Date());
        analyseScorecards.add(analyseScorecard);
    }

    /**
     * 表格 规则调用次数 统计（一天统计 一次）
     */
    private void AnalyseRuleCount() {
        //一天统计一次  按照时间统计
        List<TMonitorStrategyDTO> monitorStrategyDTOS  = monitorStrategyMapper.countRule();
        List<AnalyseRule> analyseRules = new ArrayList<>();
        for (int i = 0; i < monitorStrategyDTOS.size(); i++) {
            TMonitorStrategyDTO tMonitorStrategyDTO =  monitorStrategyDTOS.get(i);
            buildRules(tMonitorStrategyDTO,analyseRules);
        }
        analyseRuleService.saveBatch(analyseRules);

    }

    private void buildRules(TMonitorStrategyDTO tMonitorStrategyDTO, List<AnalyseRule> analyseRules) {
        AnalyseRule analyseRule = new AnalyseRule();
        analyseRule.setCallDate(tMonitorStrategyDTO.getCallDate());
        //引擎id
        analyseRule.setEngineId(tMonitorStrategyDTO.getEngineId());
        //引擎版本id
        analyseRule.setVersionId(tMonitorStrategyDTO.getEngineVersionId());
        analyseRule.setEngineName(null);
        analyseRule.setEngineDescription(null);
        analyseRule.setOrganId(tMonitorStrategyDTO.getOrganId());
        analyseRule.setRuleId(tMonitorStrategyDTO.getStrategyId());
        analyseRule.setRuleName(tMonitorStrategyDTO.getStrategyName());
        analyseRule.setRuleVersionCode(null);
        analyseRule.setRuleVersionId(null);
        analyseRule.setHitCount(tMonitorStrategyDTO.getTotal());
        analyseRule.setCreateTime(new Date());
        analyseRule.setCreateUserId(Long.valueOf(1));
        analyseRule.setUpdateUserId(Long.valueOf(1));
        analyseRule.setUpdateTime(new Date());
        analyseRules.add(analyseRule);
    }

    /**
     * 表格引擎调用 统计（一天统计一次）
     */
    private void AnalyseChartEngineCallCount() {
        //查引擎版本表 按 引擎id ,引擎版本Id,组织id 来进行分组
        List<EngineVersion> engineVersions = engineVersionMapper.selectAll();
        //循环统计  统计结果放数组中里
        List<AnalyseEngineCall> engineCalls = new ArrayList<>();
        for (int i = 0; i < engineVersions.size(); i++) {
            EngineVersion engineVersion = engineVersions.get(i);
            Long engineId = engineVersion.getEngineId();
            Long versionId = engineVersion.getVersionId();
            Engine engine1 = engineMapper.selectById(engineId);
            if(engine1==null){
                continue;
            }
            if(i%20==0){
                try {
                    TimeUnit.SECONDS.sleep(Long.valueOf("5"));//等待时
                } catch (InterruptedException e) {
                    logger.info("==============================睡眠100s");
                }
            }
            //决策引擎调用次数每天趋势  按天统计次数
            QueryWrapper<TMonitorEngine> monitorEngineQueryWrapper = new QueryWrapper<>();
            monitorEngineQueryWrapper.eq("engine_version_id",versionId);
            //统计今天
            monitorEngineQueryWrapper.le("create_time", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDays(new Date(),1)));
            monitorEngineQueryWrapper.ge("create_time", new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addDays(new Date(),0)));
            Integer countNum = monitorEngineMapper.selectCount(monitorEngineQueryWrapper);
            buildModels(engine1,versionId,engineCalls,Long.valueOf(countNum));
        }
        insertIntoDB(engineCalls);
    }

    /**
     * 表格 引擎调用执行结果 统计
     */
    private void AnalyseDecisionResultCount() {
        //决策引擎调用次数每天趋势  按天统计次数
        //统计今天
        List<AnalyseDecisionResult> analyseDecisionResults = new ArrayList<>();
        List<EngineResultSetDTO> engineResultSetList = engineVersionMapper.countDecisionResult();
        for (int i = 0; i < engineResultSetList.size(); i++) {
            EngineResultSetDTO engineResultSet =  engineResultSetList.get(i);
            buildEngineResultModels(engineResultSet,analyseDecisionResults);
        }
        analyseDecisionResultService.saveBatch(analyseDecisionResults);
    }

    private void buildEngineResultModels(EngineResultSetDTO decisionResult, List<AnalyseDecisionResult> analyseDecisionResults) {
        AnalyseDecisionResult analyseDecisionResult = new AnalyseDecisionResult();
        analyseDecisionResult.setCallDate(decisionResult.getCallDate());
        analyseDecisionResult.setResult(decisionResult.getResult());
        //引擎id
        analyseDecisionResult.setEngineId(decisionResult.getEngineId());
        analyseDecisionResult.setEngineName(decisionResult.getEngineName());
        analyseDecisionResult.setEngineDescription(null);
        //引擎版本id
        analyseDecisionResult.setVersionId(decisionResult.getEngineVersion());
        analyseDecisionResult.setOrganId(decisionResult.getOrganId());
        analyseDecisionResult.setCreateUserId(Long.valueOf(1));
        analyseDecisionResult.setUpdateUserId(Long.valueOf(1));
        analyseDecisionResult.setCreateTime(new Date());
        analyseDecisionResult.setUpdateTime(new Date());
        //引擎执行结果
        analyseDecisionResult.setResult(decisionResult.getResult());
        analyseDecisionResult.setResultCount(decisionResult.getTotal());
        analyseDecisionResults.add(analyseDecisionResult);
    }

    public void insertIntoDB( List<AnalyseEngineCall> engineCalls){
//        CompletableFuture.runAsync(()->{
        analyseEngineCallService.saveBatch(engineCalls);
//        },threadPoolTaskExecutor);
    }
    /**
     * 根据业务类型 组装业务对象
     * @param engineCalls
     * @param num
     */
    private void buildModels(Engine engine1, Long versionId, List<AnalyseEngineCall> engineCalls, Long num) {
        //组装统计信息数组
        AnalyseEngineCall analyseEngineCall = new AnalyseEngineCall();
        analyseEngineCall.setEngineId(engine1.getId());
        analyseEngineCall.setEngineName(engine1.getName());
        analyseEngineCall.setEngineDescription(engine1.getDescription());
        analyseEngineCall.setVersionId(versionId);
        analyseEngineCall.setVersionCode(null);
        analyseEngineCall.setCallCount(num);
        analyseEngineCall.setOrganId(engine1.getOrganId());
        analyseEngineCall.setCreateUserId(Long.valueOf(1));
        analyseEngineCall.setUpdateUserId(Long.valueOf(1));
        analyseEngineCall.setCreateTime(new Date());
        analyseEngineCall.setUpdateTime(new Date());
        analyseEngineCall.setCallDate(new Date());
        engineCalls.add(analyseEngineCall);
    }
}
