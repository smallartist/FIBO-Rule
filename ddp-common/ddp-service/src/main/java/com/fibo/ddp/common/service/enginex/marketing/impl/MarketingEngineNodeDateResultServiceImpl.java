package com.fibo.ddp.common.service.enginex.marketing.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.enginex.marketing.MarketingEngineNodeDateResultMapper;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineNodeDateResult;
import com.fibo.ddp.common.service.enginex.marketing.MarketingEngineNodeDateResultService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 营销引擎节点当天结果表(MarketingEngineNodeDateResult)表服务实现类
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@Service
public class MarketingEngineNodeDateResultServiceImpl extends ServiceImpl<MarketingEngineNodeDateResultMapper, MarketingEngineNodeDateResult> implements MarketingEngineNodeDateResultService {

    @Override
    public List<MarketingEngineNodeDateResult> getNodeDateResultByDate(Integer engineVersionId, Date startDate, Date endDate) {
        LambdaQueryWrapper<MarketingEngineNodeDateResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarketingEngineNodeDateResult::getEngineVersionId, engineVersionId);
        if (startDate != null && endDate != null) {
            queryWrapper.between(MarketingEngineNodeDateResult::getCurrentDate, startDate, endDate);
        }
        List<MarketingEngineNodeDateResult> list = this.list(queryWrapper);
        return list;
    }
}
