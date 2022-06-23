package com.fibo.ddp.common.service.strategyx.decisiontree;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeDetailCondition;

import java.util.Collection;
import java.util.List;

public interface DecisionTreeDetailConditionService extends IService<DecisionTreeDetailCondition> {

    boolean addDecisionTreeDetailCondition(Long detailId, List<DecisionTreeDetailCondition> conditionList);

    boolean removeByDecisionTreeDetailId(List<Long> detailIds);

    // runner
    List<DecisionTreeDetailCondition> queryByDecisionTreeDetailId(Long detailId);

    List<String> queryFieldEnByDetailIds(Collection<Long> detailIds);
}
