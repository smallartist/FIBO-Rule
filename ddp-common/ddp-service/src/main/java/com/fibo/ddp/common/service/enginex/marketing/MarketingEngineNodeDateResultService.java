package com.fibo.ddp.common.service.enginex.marketing;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineNodeDateResult;

import java.util.Date;
import java.util.List;

/**
 * 营销引擎节点当天结果表(MarketingEngineNodeDateResult)表服务接口
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
public interface MarketingEngineNodeDateResultService extends IService<MarketingEngineNodeDateResult> {

    List<MarketingEngineNodeDateResult> getNodeDateResultByDate(Integer engineVersionId, Date startDate, Date endDate);
}
