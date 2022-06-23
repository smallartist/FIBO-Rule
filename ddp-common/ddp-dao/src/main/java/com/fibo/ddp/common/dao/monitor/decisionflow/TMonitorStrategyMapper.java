package com.fibo.ddp.common.dao.monitor.decisionflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorStrategy;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorStrategyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 决策流策略层面监控 Mapper 接口
 * </p>
 */
@Mapper
public interface TMonitorStrategyMapper extends BaseMapper<TMonitorStrategy> {

    List<TMonitorStrategyDTO> countRule();

    List<TMonitorStrategyDTO> countScorecardHit();

    List<TMonitorStrategyDTO> countDecisionTables();
}
