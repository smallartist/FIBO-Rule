package com.fibo.ddp.common.service.monitor.runner.hbase.impl;

import com.alibaba.fastjson.JSON;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorStrategy;
import com.fibo.ddp.common.service.monitor.runner.hbase.IMonitorStrategyService;
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

@Service("iMonitorStrategyImpl")
public class IMonitorStrategyImpl implements IMonitorStrategyService {

    /**
     * Hbase客户端
     */
    private final HbaseTemplate hbaseTemplate;
    private static final Logger logger = LoggerFactory.getLogger(IMonitorNodeImpl.class);


    @Autowired
    public IMonitorStrategyImpl(HbaseTemplate hbaseTemplate){
        this.hbaseTemplate = hbaseTemplate;
    }
    @Override
    public boolean putMonitorStrategyToHbase(MonitorStrategy monitorStrategy) {
        if(null== hbaseTemplate){
            logger.info("putMonitorStrategyToHbase===================================hbase客户端连接中断");
            return false;
        }
        //生成行键
        String rowKeyStr = monitorStrategy.getRowKey();
////        rowKeyStr = RowKeyUtil.formatLastUpdate(new Date().getTime());
//        //todo 加校验，该行键是否已经存在，已经存在返回false,打印日志，行键已经存在
        try {
            if(hbaseTemplate.getConnection().getTable(TableName.valueOf(Constants.MonitorStrategy.TABLE_NAME)).exists(new Get(Bytes.toBytes(rowKeyStr)))){
                logger.info("putMonitorStrategyToHbase==============================Rowkey:{} 已经存在！",rowKeyStr);
                return false;
            }
        } catch (Exception e) {
            logger.error("putMonitorStrategyToHbase=============================异常信息:{}",e.getMessage());
        }
        Put put = new Put(Bytes.toBytes(rowKeyStr));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.PARAMS),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getMonitorInfo().getParams())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.SNAPSHOT),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getMonitorInfo().getSnapshot())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.RESULT),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getMonitorInfo().getResult())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.BUSINESS_ID),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getBaseInfo().getBusinessId())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.NODE_ID),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getBaseInfo().getNodeId())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.NODE_TYPE),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getBaseInfo().getNodeType())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.STRATEGY_ID),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getBaseInfo().getStrategyId())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.STRATEGY_NAME),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getBaseInfo().getStrategyName())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.STRATEGY_TYPE),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getBaseInfo().getStrategyType())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                Bytes.toBytes(Constants.MonitorStrategy.ENGINE_VERSION_ID),
                Bytes.toBytes(JSON.toJSONString(monitorStrategy.getBaseInfo().getEngineVersionId())));
        hbaseTemplate.saveOrUpdate(Constants.MonitorStrategy.TABLE_NAME,put);
        logger.info("putMonitorStrategyToHbase=================================>监控中心-策略监控信息-Hbase数据写入成功:{}",put);
        return true;
    }
}
