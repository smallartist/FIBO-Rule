package com.fibo.ddp.common.dao.monitor.decisionflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorNode;
import com.fibo.ddp.common.utils.constant.monitor.Constants;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class MonitorNodeHbaseMapper implements RowMapper<MonitorNode> {
    @Override
    public MonitorNode mapRow(Result result, int i) throws Exception {
        MonitorNode monitorNode = new MonitorNode();
        MonitorNode.BaseInfo baseInfo = new MonitorNode.BaseInfo();
        MonitorNode.MonitorInfo monitorInfo = new MonitorNode.MonitorInfo();
        monitorNode.setRowKey(Bytes.toString(result.getRow()));
        baseInfo.setBusinessId(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorNode.BUSINESS_ID)
                        )
                )
        );
        baseInfo.setNodeId(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorNode.NODE_ID)
                        )
                )
        );
        baseInfo.setNodeName(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorNode.NODE_NAME)
                        )
                )
        );
        baseInfo.setNodeType(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorNode.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorNode.NODE_TYPE)
                        )
                )
        );
        monitorNode.setBaseInfo(baseInfo);
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
        monitorNode.setMonitorInfo(monitorInfo);
        return monitorNode;
    }
}
