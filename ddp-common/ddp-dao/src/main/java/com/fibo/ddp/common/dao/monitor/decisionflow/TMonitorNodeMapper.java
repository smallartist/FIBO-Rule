package com.fibo.ddp.common.dao.monitor.decisionflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.TMonitorNodeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 决策流节点层面监控 Mapper 接口
 * </p>
 */
@Mapper
public interface TMonitorNodeMapper extends BaseMapper<TMonitorNode> {

    List<TMonitorNodeDTO> countNodeHit();
}
