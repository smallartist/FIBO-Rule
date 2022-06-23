package com.fibo.ddp.common.service.monitor.decisionflow.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fibo.ddp.common.dao.enginex.risk.EngineResultSetMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorEngineMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorNodeMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.TMonitorStrategyMapper;
import com.fibo.ddp.common.model.enginex.risk.EngineResultSet;
import com.fibo.ddp.common.model.monitor.decisionflow.DecisionFlowRequestDTO;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorEngine;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorStrategy;
import com.fibo.ddp.common.service.monitor.decisionflow.IMonitorService;
import com.fibo.ddp.common.utils.constant.monitor.MonitorStorageType;
import com.fibo.ddp.common.utils.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("monitorMysqlVersionServiceImpl")
public class MonitorMysqlVersionServiceImpl implements IMonitorService {

    @Resource
    public TMonitorEngineMapper monitorEngineMapper;
    @Resource
    public EngineResultSetMapper resultSetMapper;
    @Resource
    public TMonitorNodeMapper monitorNodeMapper;
    @Resource
    public TMonitorStrategyMapper monitorStrategyMapper;

    @Override
    public String getStorageType() {
        return MonitorStorageType.Mysql;
    }

    @Override
    public List<EngineResultSet> getEngineResultSetBySegment(Map map) {
        return resultSetMapper.getEngineResultSetBySegment(map);
    }

    @Override
    public List<?> getResultDecisionFlowDetail(DecisionFlowRequestDTO param) {
        if(param!=null && StringUtil.isBlank(param.getResultId())){
            return null;
        }
        QueryWrapper<TMonitorEngine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("monitor_parent_id",param.getResultId());
        List<TMonitorEngine> monitorEngines = monitorEngineMapper.selectList(queryWrapper);
        return monitorEngines;
    }

    @Override
    public List<?> getResultNodeDetail(DecisionFlowRequestDTO param) {
        if(param!=null && StringUtil.isBlank(param.getMonitorParentId())){
            return null;
        }
        QueryWrapper<TMonitorNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("monitor_parent_id",param.getMonitorParentId());
        queryWrapper.eq("node_id",param.getNodeId());
        List<TMonitorNode> monitorNodes = monitorNodeMapper.selectList(queryWrapper);
        return monitorNodes;
    }

    @Override
    public List<?> getResultStrategyDetail(DecisionFlowRequestDTO param) {
        if(param!=null && StringUtil.isBlank(param.getMonitorParentId())){
            return null;
        }
        QueryWrapper<TMonitorStrategy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("monitor_parent_id",param.getMonitorParentId());
        queryWrapper.eq("node_id",param.getNodeId());
        queryWrapper.eq("strategy_id",param.getStrategyId());
        List<TMonitorStrategy> monitorStrategies = monitorStrategyMapper.selectList(queryWrapper);
        return monitorStrategies;
    }
}
