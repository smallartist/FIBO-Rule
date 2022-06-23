package com.fibo.ddp.common.service.strategyx.decisiontable.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontable.DecisionTablesDetailConditionMapper;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesDetailCondition;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesDetailConditionService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.util.strategyx.CustomValueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * (DecisionTablesDetailCondition)表服务实现类
 */
@Service("decisionTablesDetailConditionService")
public class DecisionTablesDetailConditionServiceImpl extends ServiceImpl<DecisionTablesDetailConditionMapper, DecisionTablesDetailCondition> implements DecisionTablesDetailConditionService {
    @Autowired
    private RedisManager redisManager;

    @Value("${switch.use.cache}")
    private String cacheSwitch;

//    @Override
//    public List<DecisionTablesDetailCondition> queryByDecisionTablesDetailId(Long detailId) {
//        if (detailId==null){
//            return null;
//        }
//        DecisionTablesDetailCondition query = new DecisionTablesDetailCondition();
//        query.setDetailId(detailId);
//        List<DecisionTablesDetailCondition> list = this.list(new QueryWrapper<>(query));
//        return list;
//    }

    @Override
    public List<DecisionTablesDetailCondition> queryByDecisionTablesDetailId(Long detailId) {
        if (detailId==null){
            return null;
        }

        List<DecisionTablesDetailCondition> list = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TABLES_DETAIL_CONDITION, detailId);
            list = redisManager.getByForeignKey(key, DecisionTablesDetailCondition.class);
        } else {
            DecisionTablesDetailCondition query = new DecisionTablesDetailCondition();
            query.setDetailId(detailId);
            list = this.list(new QueryWrapper<>(query));
        }

        return list;
    }

    @Override
    public List<DecisionTablesDetailCondition> updateDecisionTablesDetailCondition(Long detailId, List<DecisionTablesDetailCondition> list) {
        return null;
    }

    @Override
    public List<DecisionTablesDetailCondition> insertDecisionTablesDetailCondition(Long detailId, List<DecisionTablesDetailCondition> list) {
        if (detailId==null||list==null||list.isEmpty()){
            return null;
        }
        for (DecisionTablesDetailCondition condition : list) {
            condition.setDetailId(detailId);
        }
        boolean b = this.saveBatch(list);
        if (b){
            return this.queryByDecisionTablesDetailId(detailId);
        }
        return null;
    }

    @Override
    public boolean deleteByDecisionTablesDetailId(List<Long> detailIds) {
        LambdaUpdateWrapper<DecisionTablesDetailCondition> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(DecisionTablesDetailCondition::getDetailId,detailIds);
        return this.remove(wrapper);
//        int result = decisionTablesDetailConditionMapper.deleteByDetailIds(detailIds);
//        return result>0?true:false;
    }

    @Override
    public List<String> queryFieldEnByDetailIds(List<Long> detailIds) {
        if (detailIds==null||detailIds.isEmpty()){
            return null;
        }
        List<DecisionTablesDetailCondition> list = new ArrayList<>();
        Set<String> fieldEns = new HashSet<>();
        if (Constants.switchFlag.ON.equals(cacheSwitch)) {
            for (Long detailId : detailIds) {
                String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TABLES_DETAIL_CONDITION, detailId);
                list.addAll(redisManager.getByForeignKey(key, DecisionTablesDetailCondition.class));
            }
        } else {
            LambdaQueryWrapper<DecisionTablesDetailCondition> conditionWrapper = new LambdaQueryWrapper<>();
            conditionWrapper.in(DecisionTablesDetailCondition::getDetailId, detailIds);
            list = this.list(conditionWrapper);
        }
        for (DecisionTablesDetailCondition condition : list) {
            if (condition.getVariableType()==null||condition.getVariableType()==1){
                continue;
            }
            if (condition.getVariableType() == 2) {
                fieldEns.add(condition.getFieldValue());
            } else if (condition.getVariableType() == 3) {
                fieldEns.addAll(CustomValueUtils.getFieldEnSet(condition.getFieldValue()));
            }
        }
        return new ArrayList<>(fieldEns);
    }
}
