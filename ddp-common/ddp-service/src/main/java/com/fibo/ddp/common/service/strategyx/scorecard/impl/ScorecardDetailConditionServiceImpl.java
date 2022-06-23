package com.fibo.ddp.common.service.strategyx.scorecard.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.scorecard.ScorecardDetailConditionMapper;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDetailCondition;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardDetailConditionService;
import com.fibo.ddp.common.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评分卡明细表的condition表(ScorecardDetailCondition)表服务实现类
 */
@Service
public class ScorecardDetailConditionServiceImpl extends ServiceImpl<ScorecardDetailConditionMapper, ScorecardDetailCondition> implements ScorecardDetailConditionService {

    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public List<ScorecardDetailCondition> getConditionListByDetailId(Integer detailId) {
        List<ScorecardDetailCondition> scorecardDetailConditions = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_SCORECARD_DETAIL_CONDITION, detailId);
            scorecardDetailConditions = redisManager.getByForeignKey(key, ScorecardDetailCondition.class);
        } else {
            LambdaQueryWrapper<ScorecardDetailCondition> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ScorecardDetailCondition::getDetailId, detailId);
            scorecardDetailConditions = this.list(queryWrapper);
        }
        return scorecardDetailConditions;
    }
}
