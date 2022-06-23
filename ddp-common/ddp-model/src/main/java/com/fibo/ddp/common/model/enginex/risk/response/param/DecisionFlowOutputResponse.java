package com.fibo.ddp.common.model.enginex.risk.response.param;

import lombok.Data;

import java.util.List;

/**
 * 决策流输出内容 （节点类型-》节点列表-》节点输出字段）
 *
 */
@Data
public class DecisionFlowOutputResponse {

    /**
     * 节点类型集合
     */
    private List<NodeTypeResponse> nodeTypeList;
}
