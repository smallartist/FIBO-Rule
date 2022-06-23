package com.fibo.ddp.common.service.monitor.runner.hbase.impl;

import com.alibaba.fastjson.JSON;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorDecisionFlow;
import com.fibo.ddp.common.service.monitor.runner.hbase.IMonitorDecisionFlow;
import com.fibo.ddp.common.utils.constant.monitor.Constants;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 决策流监控 写入Hbase表
 */
@Service("iMonitorDecisionFlowImpl")
public class IMonitorDecisionFlowImpl implements IMonitorDecisionFlow {
    /**
     * Hbase客户端
     */
    private final HbaseTemplate hbaseTemplate;
    private static final Logger logger = LoggerFactory.getLogger(IMonitorDecisionFlowImpl.class);


    @Autowired
    public IMonitorDecisionFlowImpl(HbaseTemplate hbaseTemplate){
        this.hbaseTemplate = hbaseTemplate;
    }


    @Override
    public boolean putMonitorDecisionFlowToHbase(MonitorDecisionFlow monitorDecisionFlow) {
        if(null== hbaseTemplate){
            logger.info("===================================hbase客户端连接中断");
            return false;
        }
        //生成行键
        String rowKeyStr = monitorDecisionFlow.getRowKey();
        //todo 加校验，该行键是否已经存在，已经存在返回false,打印日志，行键已经存在
        try {
            if(hbaseTemplate.getConnection().getTable(TableName.valueOf(Constants.MonitorDecisionFlow.TABLE_NAME)).exists(new Get(Bytes.toBytes(rowKeyStr)))){
                logger.info("=============================Rowkey:{} 已经存在！",rowKeyStr);
                return false;
            }
        } catch (Exception e) {
            logger.error("=============================putMonitorDecisionFlowToHbase");
        }
        Put put = new Put(Bytes.toBytes(rowKeyStr));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorDecisionFlow.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorDecisionFlow.PARAMS),
                Bytes.toBytes(JSON.toJSONString(monitorDecisionFlow.getMonitorInfo().getParams())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorDecisionFlow.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorDecisionFlow.SNAPSHOT),
                Bytes.toBytes(JSON.toJSONString(monitorDecisionFlow.getMonitorInfo().getSnapshot())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorDecisionFlow.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorDecisionFlow.PROCESS),
                Bytes.toBytes(JSON.toJSONString(monitorDecisionFlow.getMonitorInfo().getProcess())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorDecisionFlow.BASE_INFO),
                Bytes.toBytes(Constants.MonitorDecisionFlow.BUSINESS_ID),
                Bytes.toBytes(JSON.toJSONString(monitorDecisionFlow.getBaseInfo().getBusinessId())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorDecisionFlow.BASE_INFO),
                Bytes.toBytes(Constants.MonitorDecisionFlow.ENGINE_NAME),
                Bytes.toBytes(JSON.toJSONString(monitorDecisionFlow.getBaseInfo().getEngineName())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorDecisionFlow.BASE_INFO),
                Bytes.toBytes(Constants.MonitorDecisionFlow.ENGINE_VERSION_ID),
                Bytes.toBytes(JSON.toJSONString(monitorDecisionFlow.getBaseInfo().getEngineVersionId())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorDecisionFlow.BASE_INFO),
                Bytes.toBytes(Constants.MonitorDecisionFlow.ENGINE_INFO),
                Bytes.toBytes(JSON.toJSONString(monitorDecisionFlow.getBaseInfo().getEngineInfo())));
        hbaseTemplate.saveOrUpdate(Constants.MonitorDecisionFlow.TABLE_NAME,put);
        logger.info("===================================================== Hbase数据写入成功:{}",put);
        return true;
    }
}
