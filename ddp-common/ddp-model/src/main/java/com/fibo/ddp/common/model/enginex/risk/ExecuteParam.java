package com.fibo.ddp.common.model.enginex.risk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteParam {
    private Long engineId;//引擎id

    private Long organId;//组织id

    private String biz_enc = "0";//加解密

    private Long timestamp ;//时间戳

    private String businessId;//业务id

    private  Map fields;//指标
}
