package com.fibo.ddp.common.dao.monitor.decisionflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.monitor.decisionflow.MonitorDecisionFlow;
import com.fibo.ddp.common.utils.constant.monitor.Constants;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class MonitorDecisionFlowHbaseMapper implements RowMapper<MonitorDecisionFlow>{

    @Override
    public MonitorDecisionFlow mapRow(Result result, int i) throws Exception {
        MonitorDecisionFlow monitorDecisionFlow = new MonitorDecisionFlow();
        MonitorDecisionFlow.BaseInfo baseInfo = new MonitorDecisionFlow.BaseInfo();
        MonitorDecisionFlow.MonitorInfo monitorInfo = new MonitorDecisionFlow.MonitorInfo();
        monitorDecisionFlow.setRowKey(Bytes.toString(result.getRow()));
        baseInfo.setBusinessId(
                Bytes.toString(
                    result.getValue(
                        Bytes.toBytes(Constants.MonitorDecisionFlow.BASE_INFO),
                        Bytes.toBytes(Constants.MonitorDecisionFlow.BUSINESS_ID)
                    )
                )
        );
        baseInfo.setEngineName(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorDecisionFlow.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorDecisionFlow.ENGINE_NAME)
                        )
                )
        );
        baseInfo.setEngineVersionId(
                Bytes.toString(
                        result.getValue(
                                Bytes.toBytes(Constants.MonitorDecisionFlow.BASE_INFO),
                                Bytes.toBytes(Constants.MonitorDecisionFlow.ENGINE_VERSION_ID)
                        )
                )
        );
        monitorDecisionFlow.setBaseInfo(baseInfo);
        monitorInfo.setParams(
                JSONObject.parseObject(
                        Bytes.toString(
                            result.getValue(
                                    Bytes.toBytes(Constants.MonitorDecisionFlow.MONITOR_INFO),
                                    Bytes.toBytes(Constants.MonitorDecisionFlow.PARAMS)
                )
        )));
        monitorInfo.setProcess(
                JSONArray.parseArray(
                        Bytes.toString(
                                result.getValue(
                                        Bytes.toBytes(Constants.MonitorDecisionFlow.MONITOR_INFO),
                                        Bytes.toBytes(Constants.MonitorDecisionFlow.PROCESS)
                                )
                        )
                        , String.class
                ));
        monitorInfo.setSnapshot(
                JSONArray.parseArray(
                        Bytes.toString(
                                result.getValue(
                                        Bytes.toBytes(Constants.MonitorDecisionFlow.MONITOR_INFO),
                                        Bytes.toBytes(Constants.MonitorDecisionFlow.SNAPSHOT)
                                )
                        )
                        , EngineNode.class
                ));
        monitorDecisionFlow.setMonitorInfo(monitorInfo);
        return monitorDecisionFlow;
    }
}
