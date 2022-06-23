package com.fibo.ddp.common.model.enginex.risk.request;

import com.fibo.ddp.common.model.common.BaseParam;
import lombok.Data;

@Data
public class EngineListParam extends BaseParam {

    private Long nodeId;
    private Long engineId;
    private String searchString;
}
