package com.fibo.ddp.common.model.enginex.dataflow.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDispose {
    /**
     * 触达方式 Sms、App、WebHook、WeChat
     */
    private String touchType ;
    /**
     * 模板编码
     */
    private String templateCode;
    /**
     * 变量配置
     */
    private Map<String, String> params ;
}
