package com.fibo.ddp.common.service.monitor.runner.hbase;

import com.fibo.ddp.common.model.enginex.risk.Engine;

import java.util.Map;

public interface IFeatureRecordService {
    /**
     * 全量指标入库
     * @param featureMaps 执行前后指标map
     * @param engine 引擎节点
     * @param paramJson 整个执行器方法的入参
     */
    void recordAllFeature(Map<String, Map<String, Object>> featureMaps, Engine engine, Map<String, Object> paramJson);
}
