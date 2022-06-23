package com.fibo.ddp.enginex.riskengine.runner.business;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DecisionApiRequest {

    private String tp_code; // 调用方编码
    private String timestamp; // 精确到毫秒
    private String sign; // 签名
    private String biz_enc; // biz_data加密方式（0不加密，1加密）
    private DecisionApiBizData biz_data; // 请求的业务数据，json格式的字符串
}
