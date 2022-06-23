package com.fibo.ddp.common.service.monitor.decisionflow.impl;

import com.fibo.ddp.common.dao.enginex.risk.EngineResultSetMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.MonitorDecisionFlowHbaseMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.MonitorNodeHbaseMapper;
import com.fibo.ddp.common.dao.monitor.decisionflow.MonitorStrategyHbaseMapper;
import com.fibo.ddp.common.model.enginex.risk.EngineResultSet;
import com.fibo.ddp.common.model.monitor.decisionflow.DecisionFlowRequestDTO;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorDecisionFlow;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorStrategy;
import com.fibo.ddp.common.service.monitor.decisionflow.IMonitorService;
import com.fibo.ddp.common.utils.constant.monitor.Constants;
import com.fibo.ddp.common.utils.constant.monitor.MonitorStorageType;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("monitorServiceImpl")
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    public HbaseTemplate hbaseTemplate;
    @Resource
    public EngineResultSetMapper resultSetMapper;

    @Override
    public String getStorageType() {
        return MonitorStorageType.HBase;
    }

    @Override
    public List<EngineResultSet> getEngineResultSetBySegment(Map map) {
        return resultSetMapper.getEngineResultSetBySegment(map);
    }

    @Override
    public List<?> getResultDecisionFlowDetail(DecisionFlowRequestDTO param) {
        Scan scan = new Scan(Bytes.toBytes(param.getHbaseRowKey()),Bytes.toBytes(param.getHbaseRowKey()));
        //根据条件查询Hbase表中信息。
        List<MonitorDecisionFlow> monitorDecisionFlows = hbaseTemplate.find(
                Constants.MonitorDecisionFlow.TABLE_NAME,
                scan, new MonitorDecisionFlowHbaseMapper());
        return monitorDecisionFlows;
    }

    @Override
    public List<?> getResultNodeDetail(DecisionFlowRequestDTO param) {
        Scan scan = new Scan(Bytes.toBytes(param.getHbaseRowKey()),Bytes.toBytes(param.getHbaseRowKey()));
//        Scan scan = new Scan();
        //根绝条件查询Hbase表中信息
        List<MonitorNode> monitorNodes = hbaseTemplate.find(
                Constants.MonitorNode.TABLE_NAME,
                scan,new MonitorNodeHbaseMapper());
        return monitorNodes;
    }

    @Override
    public List<?> getResultStrategyDetail(DecisionFlowRequestDTO param) {
        Scan scan = new Scan(Bytes.toBytes(param.getHbaseRowKey()),Bytes.toBytes(param.getHbaseRowKey()));
//        Scan scan = new Scan();
        //根绝条件查询Hbase表中信息
        List<MonitorStrategy> monitorStrategies = hbaseTemplate.find(
                Constants.MonitorStrategy.TABLE_NAME,
                scan,new MonitorStrategyHbaseMapper());
        //返回

        return monitorStrategies;
    }

}
