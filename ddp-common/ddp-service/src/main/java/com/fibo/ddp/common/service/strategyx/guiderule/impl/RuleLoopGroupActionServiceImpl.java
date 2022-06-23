package com.fibo.ddp.common.service.strategyx.guiderule.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.guiderule.RuleLoopGroupActionMapper;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleLoopGroupAction;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleLoopGroupActionService;
import com.fibo.ddp.common.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (RuleLoopGroupAction)表服务实现类
 */
@Service("ruleLoopGroupActionService")
public class RuleLoopGroupActionServiceImpl extends ServiceImpl<RuleLoopGroupActionMapper, RuleLoopGroupAction> implements RuleLoopGroupActionService {
    @Resource
    private RuleLoopGroupActionMapper ruleLoopGroupActionMapper;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

//    @Override
//    public List<RuleLoopGroupAction> getRuleLoopList(Long forId, Long conditionId) {
//        RuleLoopGroupAction ruleLoopGroupAction = new RuleLoopGroupAction();
//        ruleLoopGroupAction.setConditionForId(forId);
//        ruleLoopGroupAction.setConditionGroupId(conditionId);
//        List<RuleLoopGroupAction> loopList = ruleLoopGroupActionMapper.selectList(new QueryWrapper<>(ruleLoopGroupAction));
//        if (loopList==null){
//            loopList = new ArrayList<>();
//        }
//        return loopList;
//    }

    @Override
    public List<RuleLoopGroupAction> getRuleLoopList(Long forId, Long conditionId) {
        List<RuleLoopGroupAction> loopList = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_RULE_LOOP_GROUP_ACTION, forId);
            loopList = redisManager.getByForeignKey(key, RuleLoopGroupAction.class);
            loopList = loopList.stream().filter(item -> item.getConditionGroupId().equals(conditionId)).collect(Collectors.toList());
        } else {
            RuleLoopGroupAction ruleLoopGroupAction = new RuleLoopGroupAction();
            ruleLoopGroupAction.setConditionForId(forId);
            ruleLoopGroupAction.setConditionGroupId(conditionId);
            loopList = ruleLoopGroupActionMapper.selectList(new QueryWrapper<>(ruleLoopGroupAction));
        }

        if (loopList==null){
            loopList = new ArrayList<>();
        }
        return loopList;
    }

    @Override
    public boolean addLoopGroupList(Long forId,Long conditionId, List<RuleLoopGroupAction> loopGroupActions) {
        for (RuleLoopGroupAction loopGroupAction : loopGroupActions) {
            loopGroupAction.setConditionForId(forId);
            loopGroupAction.setConditionGroupId(conditionId);
        }
        boolean add = this.saveBatch(loopGroupActions);
        return add;
    }

    @Override
    public boolean deleteLoopGroupByForId(Long forId) {
        if (forId==null){
            return false;
        }
        RuleLoopGroupAction ruleLoopGroupAction = new RuleLoopGroupAction();
        ruleLoopGroupAction.setConditionForId(forId);
        ruleLoopGroupActionMapper.delete(new QueryWrapper<>(ruleLoopGroupAction));
        return true;
    }
}
