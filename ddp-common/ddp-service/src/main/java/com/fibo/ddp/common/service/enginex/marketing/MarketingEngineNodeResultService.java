package com.fibo.ddp.common.service.enginex.marketing;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineNodeResult;

import java.util.List;

/**
 * 营销引擎节点结果表(MarketingEngineNodeResult)表服务接口
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
public interface MarketingEngineNodeResultService extends IService<MarketingEngineNodeResult> {

    List<MarketingEngineNodeResult> getNodeResultByVersionId(Integer engineVersionId);
}
