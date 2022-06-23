package com.fibo.ddp.common.model.enginex.dataflow.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataFlowEngineContentVo  {
    /**
     * 资源id
     */
    private Long sourceId = 0L;
    /**
     * 节点列表
     */
    private List<DataFlowNodeVo> nodeList = new ArrayList<>();
    /**
     * 事件匹配处理
     */
    private EventDispose eventDispose = new EventDispose("","",new HashMap<>());
}
