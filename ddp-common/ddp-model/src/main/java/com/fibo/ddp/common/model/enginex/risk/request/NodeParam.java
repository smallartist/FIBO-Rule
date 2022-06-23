package com.fibo.ddp.common.model.enginex.risk.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeParam {
    private Long currentNodeId;//当前节点id
    private String preNodeId;//前一个节点id，逗号分隔
}
