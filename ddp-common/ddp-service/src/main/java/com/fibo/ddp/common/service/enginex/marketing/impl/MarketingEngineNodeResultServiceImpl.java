package com.fibo.ddp.common.service.enginex.marketing.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.enginex.marketing.MarketingEngineNodeResultMapper;
import com.fibo.ddp.common.model.enginex.marketing.entity.MarketingEngineNodeResult;
import com.fibo.ddp.common.service.enginex.marketing.MarketingEngineNodeResultService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 营销引擎节点结果表(MarketingEngineNodeResult)表服务实现类
 *
 * @author andy.wang
 * @since 2022-01-07 18:13:24
 */
@Service
public class MarketingEngineNodeResultServiceImpl extends ServiceImpl<MarketingEngineNodeResultMapper, MarketingEngineNodeResult> implements MarketingEngineNodeResultService {

    @Override
    public List<MarketingEngineNodeResult> getNodeResultByVersionId(Integer engineVersionId) {
        LambdaQueryWrapper<MarketingEngineNodeResult> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarketingEngineNodeResult::getEngineVersionId, engineVersionId);
        List<MarketingEngineNodeResult> list = this.list(queryWrapper);
        return list;
    }
}
