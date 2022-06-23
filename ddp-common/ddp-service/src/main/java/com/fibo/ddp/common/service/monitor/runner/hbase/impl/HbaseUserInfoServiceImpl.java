package com.fibo.ddp.common.service.monitor.runner.hbase.impl;

import com.fibo.ddp.common.model.monitor.decisionflow.UserInfo;
import com.fibo.ddp.common.service.monitor.runner.hbase.IHbaseUserInfoService;
import com.fibo.ddp.common.utils.constant.monitor.Constants;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import groovy.util.logging.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Slf4j
@Service("hbaseUserInfoServiceImpl")
public class HbaseUserInfoServiceImpl implements IHbaseUserInfoService {
    /**
     * Hbase客户端
     */
    private final HbaseTemplate hbaseTemplate;

    private static final Logger logger = LoggerFactory.getLogger(HbaseUserInfoServiceImpl.class);


    @Autowired
    public HbaseUserInfoServiceImpl(HbaseTemplate hbaseTemplate){
        this.hbaseTemplate = hbaseTemplate;
    }

    @Override
    public boolean dropUserInfoTemplateToHbase(UserInfo userInfo) {
        if(null== hbaseTemplate){
            return false;
        }
        int  random = new Random(10000).nextInt();
        Put put = new Put(Bytes.toBytes(new Date().getTime()+random));
        put.addColumn(Bytes.toBytes(Constants.UserInfoTable.FAMILY_BASE_INFO),Bytes.toBytes(Constants.UserInfoTable.BASEINFO),Bytes.toBytes(userInfo.getBaseInfo().getBaseInfo()));
//        System.setProperty("hadoop.home.dir", "/usr/local/hadoop/");
        hbaseTemplate.saveOrUpdate(Constants.UserInfoTable.TABLE_NAME,put);
        logger.info("=====================================================String is good:{}",put);
        return false;
    }
}
