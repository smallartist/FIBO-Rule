package com.fibo.ddp.common.model.enginex.risk.response.param;

import lombok.Data;

import java.util.List;

@Data
public class RuleOutputResponse {

    /**
     * 规则的统计信息
     */
    private List<NodeStrategyOutputResponse> statisticsOutputList;

    /**
     * 规则信息
     */
    private List<RuleInfoOutputResponse> ruleInfoList;
}
