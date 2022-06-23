package com.fibo.ddp.common.service.strategyx.decisiontree.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontree.DecisionTreeDetailConditionMapper;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeDetailCondition;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeDetailConditionService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.util.strategyx.CustomValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("decisionTreeDetailConditionService")
public class DecisionTreeDetailConditionServiceImpl extends ServiceImpl<DecisionTreeDetailConditionMapper, DecisionTreeDetailCondition> implements DecisionTreeDetailConditionService {

    @Autowired
    private DecisionTreeDetailConditionMapper conditionMapper;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

//    @Override
//    public List<DecisionTreeDetailCondition> queryByDecisionTreeDetailId(Long detailId) {
//        if (detailId == null) {
//            return null;
//        }
//        LambdaQueryWrapper<DecisionTreeDetailCondition> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(DecisionTreeDetailCondition::getDetailId, detailId);
//        return conditionMapper.selectList(wrapper);
//    }

    @Override
    public List<DecisionTreeDetailCondition> queryByDecisionTreeDetailId(Long detailId) {
        if (detailId == null) {
            return null;
        }
        List<DecisionTreeDetailCondition> list = null;
        if (Constants.switchFlag.ON.equals(cacheSwitch)) {
            String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TREE_DETAIL_CONDITION, detailId);
            list = redisManager.getByForeignKey(key, DecisionTreeDetailCondition.class);
        } else {
            LambdaQueryWrapper<DecisionTreeDetailCondition> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DecisionTreeDetailCondition::getDetailId, detailId);
            list = conditionMapper.selectList(wrapper);
        }
        return list;
    }

    @Override
    public boolean addDecisionTreeDetailCondition(Long detailId, List<DecisionTreeDetailCondition> list) {
        if (detailId == null || list == null || list.isEmpty()) {
            return false;
        }
        for (DecisionTreeDetailCondition condition : list) {
            condition.setDetailId(detailId);
        }
        return this.saveBatch(list);
    }

    @Override
    public boolean removeByDecisionTreeDetailId(List<Long> detailIds) {
        if (detailIds == null || detailIds.isEmpty()) {
            return false;
        }
        LambdaQueryWrapper<DecisionTreeDetailCondition> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DecisionTreeDetailCondition::getDetailId, detailIds);
        int delete = conditionMapper.delete(wrapper);
        return delete > 0 ? true : false;
    }

    @Override
    public List<String> queryFieldEnByDetailIds(Collection<Long> detailIds) {
        if (detailIds==null||detailIds.isEmpty()){
            return null;
        }
        List<DecisionTreeDetailCondition> list = new ArrayList<>();
        Set<String> fieldEns = new HashSet<>();
        if (Constants.switchFlag.ON.equals(cacheSwitch)) {
            for (Long detailId : detailIds) {
                String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TREE_DETAIL_CONDITION, detailId);
                list.addAll(redisManager.getByForeignKey(key, DecisionTreeDetailCondition.class));
            }
        } else {
            LambdaQueryWrapper<DecisionTreeDetailCondition> conditionWrapper = new LambdaQueryWrapper<>();
            conditionWrapper.in(DecisionTreeDetailCondition::getDetailId, detailIds);
            list = this.list(conditionWrapper);
        }
        for (DecisionTreeDetailCondition condition : list) {
            if (condition.getVariableType() == 2) {
                fieldEns.add(condition.getFieldValue());
            } else if (condition.getVariableType() == 3) {
                fieldEns.addAll(CustomValueUtils.getFieldEnSet(condition.getFieldValue()));
            }
        }
        return new ArrayList<>(fieldEns);
    }
}
