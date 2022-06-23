package com.fibo.ddp.common.service.strategyx.scorecard.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.scorecard.ScorecardDetailMapper;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDetail;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardDetailService;
import com.fibo.ddp.common.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评分卡明细表(ScorecardDetail)表服务实现类
 */
@Service
public class ScorecardDetailServiceImpl extends ServiceImpl<ScorecardDetailMapper, ScorecardDetail> implements ScorecardDetailService {

    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public List<ScorecardDetail> getDetailListByDimensionId(Integer dimensionId) {
        List<ScorecardDetail> scorecardDetails = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_SCORECARD_DETAIL, dimensionId);
            scorecardDetails = redisManager.getByForeignKey(key, ScorecardDetail.class);
        } else {
            LambdaQueryWrapper<ScorecardDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScorecardDetail::getDimensionId, dimensionId);
            scorecardDetails = this.list(queryWrapper);
        }
        return scorecardDetails;
    }

    @Override
    public List<ScorecardDetail> getDetailListByDimensionIds(List<Integer> dimensionIds) {
        List<ScorecardDetail> scorecardDetails = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            List<Long> dimensionIdList = dimensionIds.stream().map(item -> Long.valueOf(item)).collect(Collectors.toList());
            List<String> keys = RedisUtils.getForeignKey(TableEnum.T_SCORECARD_DETAIL, dimensionIdList);
            scorecardDetails = redisManager.hgetAllBatchByForeignKeys(keys, ScorecardDetail.class);
        } else {
            LambdaQueryWrapper<ScorecardDetail> detailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            detailLambdaQueryWrapper.in(ScorecardDetail::getDimensionId, dimensionIds);
            scorecardDetails = this.list(detailLambdaQueryWrapper);
        }
        return scorecardDetails;
    }
}
