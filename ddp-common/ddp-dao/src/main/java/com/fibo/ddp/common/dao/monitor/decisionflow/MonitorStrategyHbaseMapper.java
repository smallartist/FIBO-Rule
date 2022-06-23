package com.fibo.ddp.common.dao.monitor.decisionflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorStrategy;
import com.fibo.ddp.common.utils.constant.monitor.Constants;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class MonitorStrategyHbaseMapper implements RowMapper<MonitorStrategy> {

    @Override
    public MonitorStrategy mapRow(Result result, int i) throws Exception {
        MonitorStrategy monitorStrategy = new MonitorStrategy();
        MonitorStrategy.BaseInfo baseInfo = new MonitorStrategy.BaseInfo();
        MonitorStrategy.MonitorInfo monitorInfo = new MonitorStrategy.MonitorInfo();
        monitorStrategy.setRowKey(Bytes.toString(result.getRow()));
        //业务id
        baseInfo.setBusinessId(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorStrategy.BUSINESS_ID)
                        )
                )
        );
        //策略id
        baseInfo.setStrategyId(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorStrategy.STRATEGY_ID)
                        )
                )
        );
        //策略类型
        baseInfo.setStrategyType(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorStrategy.STRATEGY_TYPE)
                        )
                )
        );
        //策略名称
        baseInfo.setStrategyName(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorStrategy.STRATEGY_NAME)
                        )
                )
        );
        //节点id
        baseInfo.setNodeId(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorStrategy.NODE_ID)
                        )
                )
        );
        //节点类型
        baseInfo.setNodeType(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorStrategy.NODE_TYPE)
                        )
                )
        );
        //引擎版本Id
        baseInfo.setEngineVersionId(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorStrategy.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorStrategy.ENGINE_VERSION_ID)
                        )
                )
        );
        monitorStrategy.setBaseInfo(baseInfo);
        monitorInfo.setParams(
                JSONObject.parseObject(
                        Bytes.toString(
                                result.getValue(
                                        Bytes.toBytes(Constants.MonitorNode.MONITOR_INFO),
                                        Bytes.toBytes(Constants.MonitorNode.PARAMS)
                                )
                        )));
        monitorInfo.setResult(
                JSON.parseObject(
                        Bytes.toString(
                                result.getValue(
                                        Bytes.toBytes(Constants.MonitorNode.MONITOR_INFO),
                                        Bytes.toBytes(Constants.MonitorNode.RESULT)
                                )
                        )
                )
        );
        monitorInfo.setSnapshot(
                JSON.parseObject(
                        Bytes.toString(
                                result.getValue(
                                        Bytes.toBytes(Constants.MonitorNode.MONITOR_INFO),
                                        Bytes.toBytes(Constants.MonitorNode.SNAPSHOT)
                                )
                        )
                )
        );
        monitorStrategy.setMonitorInfo(monitorInfo);
        return monitorStrategy;
    }
}
