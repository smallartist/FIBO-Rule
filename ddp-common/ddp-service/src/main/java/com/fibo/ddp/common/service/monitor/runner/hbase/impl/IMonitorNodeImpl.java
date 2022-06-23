package com.fibo.ddp.common.service.monitor.runner.hbase.impl;

import com.alibaba.fastjson.JSON;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.service.monitor.runner.hbase.IMonitorNode;
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

@Service("iMonitorNodeImpl")
public class IMonitorNodeImpl implements IMonitorNode {
    /**
     * Hbase客户端
     */
    private final HbaseTemplate hbaseTemplate;
    private static final Logger logger = LoggerFactory.getLogger(IMonitorNodeImpl.class);


    @Autowired
    public IMonitorNodeImpl(HbaseTemplate hbaseTemplate){
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public boolean putMonitorNodeToHbase(MonitorNode monitorNode) {
        if(null== hbaseTemplate){
            logger.info("===================================hbase客户端连接中断");
            return false;
        }
        //生成行键
        String rowKeyStr = monitorNode.getRowKey();
//        rowKeyStr = RowKeyUtil.formatLastUpdate(new Date().getTime());
        //todo 加校验，该行键是否已经存在，已经存在返回false,打印日志，行键已经存在
        try {
            if(hbaseTemplate.getConnection().getTable(TableName.valueOf(Constants.MonitorNode.TABLE_NAME)).exists(new Get(Bytes.toBytes(rowKeyStr)))){
                logger.info("==============================行键:{} 已经存在！",rowKeyStr);
                return false;
            }
        } catch (Exception e) {
            logger.error("=============================putMonitorNodeToHbase:{}",e.getMessage());
        }
        Put put = new Put(Bytes.toBytes(rowKeyStr));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorNode.PARAMS),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getMonitorInfo().getParams())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorNode.SNAPSHOT),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getMonitorInfo().getSnapshot())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.MONITOR_INFO),
                Bytes.toBytes(Constants.MonitorNode.RESULT),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getMonitorInfo().getResult())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                Bytes.toBytes(Constants.MonitorNode.BUSINESS_ID),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getBaseInfo().getBusinessId())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                Bytes.toBytes(Constants.MonitorNode.NODE_ID),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getBaseInfo().getNodeId())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                Bytes.toBytes(Constants.MonitorNode.NODE_NAME),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getBaseInfo().getNodeName())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                Bytes.toBytes(Constants.MonitorNode.NODE_TYPE),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getBaseInfo().getNodeType())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                Bytes.toBytes(Constants.MonitorNode.NODE_INFO),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getBaseInfo().getNodeInfo())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                Bytes.toBytes(Constants.MonitorNode.ENGINE_VERSION_ID),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getBaseInfo().getEngineVersionId())));
        put.addColumn(
                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                Bytes.toBytes(Constants.MonitorNode.ENGINE_INFO),
                Bytes.toBytes(JSON.toJSONString(monitorNode.getBaseInfo().getEngineInfo())));
        hbaseTemplate.saveOrUpdate(Constants.MonitorNode.TABLE_NAME,put);
        logger.info("=================================>putMonitorNodeToHbase: Hbase数据写入成功:{}",put);
        return true;
    }
}
