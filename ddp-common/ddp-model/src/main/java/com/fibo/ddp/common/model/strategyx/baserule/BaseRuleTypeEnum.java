package com.fibo.ddp.common.model.strategyx.baserule;

import lombok.Getter;

@Getter
public enum BaseRuleTypeEnum {

    STRATEGY_TAG_CONDITION("strategy_tag_condition","策略--标签的条件"),
    DATA_FLOW_ENGINE("data_flow_engine","数据流引擎--节点的条件"),
    MARKETING_ENGINE("marketing_engine","营销引擎--节点的条件");

    private String type;
    private String desc;

    BaseRuleTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
