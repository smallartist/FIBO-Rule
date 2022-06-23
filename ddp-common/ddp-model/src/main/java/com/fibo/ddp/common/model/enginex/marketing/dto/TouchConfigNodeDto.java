package com.fibo.ddp.common.model.enginex.marketing.dto;

import com.fibo.ddp.common.model.strategyx.baserule.BaseRule;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 触达配置节点dto
 */
@Data
public class TouchConfigNodeDto implements Serializable {
    private static final long serialVersionUID = -3512433227550450679L;
    /**
     * 节点id
     */
    private Long nodeId;
    /**
     * 用户筛选规则
     */
    private BaseRule userSelectRule;
    /**
     * 条件id
     */
    private Long userSelectRuleId;
    /**
     * 触达方式 Sms、App、WebHook、WeChat
     */
    private String touchType;
    /**
     * 模板编码
     */
    private String templateCode;
    /**
     * 变量配置
     */
    private Map<String, String> params;
}
