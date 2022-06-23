package com.fibo.ddp.common.model.strategyx.dataclean.param;

import lombok.Data;

import java.util.Map;

@Data
public class RunnerDataCleanParam {
    //需要执行的集合操作id
//    private Long id;
//    private Long versionId;
    private String dataCleanCode;
    private String versionCode;
    private Long organId;

    private Map<String,Object> param;
}
