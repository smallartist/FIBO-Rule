package com.fibo.ddp.common.model.monitor.decisionflow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {

    /**
     *基本信息
     */
    private BaseInfo baseInfo;

    @Data
    @AllArgsConstructor
    public static class BaseInfo{
        private String baseInfo;
    }
}
