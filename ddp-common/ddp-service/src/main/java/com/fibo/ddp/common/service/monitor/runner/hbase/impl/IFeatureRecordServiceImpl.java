package com.fibo.ddp.common.service.monitor.runner.hbase.impl;

import com.fibo.ddp.common.model.enginex.risk.Engine;
import com.fibo.ddp.common.service.monitor.runner.hbase.IFeatureRecordService;
import com.fibo.ddp.common.utils.constant.monitor.Constants;
import com.fibo.ddp.common.utils.constant.monitor.RowKeyUtil;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service("iFeatureRecordServiceImpl")
public class IFeatureRecordServiceImpl implements IFeatureRecordService {

    /**
     * Hbase客户端
     */
    private final HbaseTemplate hbaseTemplate;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private static final Logger logger = LoggerFactory.getLogger(IFeatureRecordServiceImpl.class);

    @Autowired
    public IFeatureRecordServiceImpl(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public void recordAllFeature(Map<String, Map<String, Object>> featureMaps, Engine engine, Map<String, Object> paramJson) {
        CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> beforeMap = featureMaps.get("before");
                Map<String, Object> afterMap = featureMaps.get("after");
                String EngineVersionId = paramJson.get("versionId") + "";
                if (null == hbaseTemplate) {
                    logger.info("===================================recordAllFeature==hbase客户端连接中断");
                    return;
                }
                //生成行键
                String rowKeyStr = null;
                rowKeyStr = RowKeyUtil.formatLastUpdate(new Date().getTime());
                // 加校验，该行键是否已经存在，已经存在返回false,打印日志，行键已经存在
                try {
                    if (hbaseTemplate.getConnection().getTable(TableName.valueOf(Constants.RunnerFeatureRecord.TABLE_NAME)).exists(new Get(Bytes.toBytes(rowKeyStr)))) {
                        logger.info("===================================recordAllFeature，行键:{} 已经存在！", rowKeyStr);
                        return;
                    }
                } catch (Exception e) {
                    logger.error("=============================recordAllFeature，异常信息:{}", e.getMessage());
                }
                Put put = new Put(Bytes.toBytes(rowKeyStr));
                //动态增加执行前指标集
                for (Map.Entry<String, Object> entry : beforeMap.entrySet()) {
                    put.addColumn(
                            Bytes.toBytes(Constants.RunnerFeatureRecord.BEFORE_FEATURE),
                            Bytes.toBytes(entry.getKey()),
                            Bytes.toBytes(entry.getValue() + ""));
                    logger.info("====================================recordAllFeature=执行前指标：key,{},value,{}", entry.getKey(), entry.getValue());
                }
                //动态增加执行后指标集
                for (Map.Entry<String, Object> entry : afterMap.entrySet()) {
                    put.addColumn(
                            Bytes.toBytes(Constants.RunnerFeatureRecord.AFTER_FEATURE),
                            Bytes.toBytes(entry.getKey()),
                            Bytes.toBytes(entry.getValue() + ""));
                    logger.info("====================================recordAllFeature=执行后指标：key,{},value,{}", entry.getKey(), entry.getValue());
                }
                put.addColumn(
                        Bytes.toBytes(Constants.RunnerFeatureRecord.BASE_INFO),
                        Bytes.toBytes(Constants.RunnerFeatureRecord.BUSINESS_ID),
                        Bytes.toBytes(paramJson.get("pid") + ""));
                put.addColumn(
                        Bytes.toBytes(Constants.RunnerFeatureRecord.BASE_INFO),
                        Bytes.toBytes(Constants.RunnerFeatureRecord.ENGINE_ID),
                        Bytes.toBytes(engine.getId()));
                put.addColumn(
                        Bytes.toBytes(Constants.RunnerFeatureRecord.BASE_INFO),
                        Bytes.toBytes(Constants.RunnerFeatureRecord.ENGINE_NAME),
                        Bytes.toBytes(engine.getName()));
                put.addColumn(
                        Bytes.toBytes(Constants.RunnerFeatureRecord.BASE_INFO),
                        Bytes.toBytes(Constants.RunnerFeatureRecord.ENGINE_VERSION_ID),
                        Bytes.toBytes(EngineVersionId));
                logger.info("====================================recordAllFeature=即将插入数据，{}", put);
                hbaseTemplate.saveOrUpdate(Constants.RunnerFeatureRecord.TABLE_NAME, put);
                logger.info("====================================recordAllFeature=写入成功！！！！！！！！！！！！");
                paramJson.remove("versionId");
            } catch (Exception e) {
                logger.info("=============recordAllFeature:全量指标入库出错:{}", e);
            }
        }, threadPoolTaskExecutor);
    }
}
