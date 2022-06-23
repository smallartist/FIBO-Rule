package com.fibo.ddp.common.service.monitor.runner.hbase;

import com.fibo.ddp.common.model.monitor.decisionflow.UserInfo;

/**
 * user_info Hbase服务
  */
public interface IHbaseUserInfoService {

    /**
     * 将userInfo 写入Hbase
     * @param userInfo
     * @return
     */
    boolean dropUserInfoTemplateToHbase(UserInfo userInfo);
}
