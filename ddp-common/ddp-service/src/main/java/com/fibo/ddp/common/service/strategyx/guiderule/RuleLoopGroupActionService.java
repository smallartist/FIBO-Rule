package com.fibo.ddp.common.service.strategyx.guiderule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleLoopGroupAction;

import java.util.List;

/**
 * (RuleLoopGroupAction)表服务接口
 */
public interface RuleLoopGroupActionService extends IService<RuleLoopGroupAction> {

    boolean addLoopGroupList(Long forId, Long conditionId, List<RuleLoopGroupAction> loopGroupActions);
    boolean deleteLoopGroupByForId(Long forId);

    // runner
    List<RuleLoopGroupAction> getRuleLoopList(Long forId, Long conditionId);
}
