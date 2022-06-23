package com.fibo.ddp.common.model.enginex.dataflow.vo;

import com.fibo.ddp.common.model.enginex.dataflow.EngineVersionContent;
import com.fibo.ddp.common.model.enginex.marketing.vo.MarketingEngineContentVo;
import lombok.Data;

@Data
public class EngineVersionContentVo extends EngineVersionContent {
    /**
     * 数据流引擎参数
     */
    private DataFlowEngineContentVo dataFlowEngineContentVo;

    /**
     * 营销引擎参数
     */
    private MarketingEngineContentVo marketingEngineContentVo;
}
