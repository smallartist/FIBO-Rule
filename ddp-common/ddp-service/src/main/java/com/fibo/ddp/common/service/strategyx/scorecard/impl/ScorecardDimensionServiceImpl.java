package com.fibo.ddp.common.service.strategyx.scorecard.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.scorecard.ScorecardDimensionMapper;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDimension;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardDimensionService;
import com.fibo.ddp.common.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScorecardDimensionServiceImpl extends ServiceImpl<ScorecardDimensionMapper, ScorecardDimension> implements ScorecardDimensionService {

    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public List<ScorecardDimension> getDimensionListByVersionId(Long versionId) {
        List<ScorecardDimension> dimensionList = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_SCORECARD_DIMENSION, versionId);
            dimensionList = redisManager.getByForeignKey(key, ScorecardDimension.class);
        } else {
            LambdaQueryWrapper<ScorecardDimension> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScorecardDimension::getVersionId, versionId);
            dimensionList = this.list(queryWrapper);
        }
        return dimensionList;
    }
}
