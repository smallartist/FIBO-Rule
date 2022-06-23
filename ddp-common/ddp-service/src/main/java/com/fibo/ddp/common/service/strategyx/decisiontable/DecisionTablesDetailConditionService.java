package com.fibo.ddp.common.service.strategyx.decisiontable;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesDetailCondition;

import java.util.List;

/**
 * (DecisionTablesDetailCondition)表服务接口
 */
public interface DecisionTablesDetailConditionService  extends IService<DecisionTablesDetailCondition> {
    List<DecisionTablesDetailCondition> updateDecisionTablesDetailCondition(Long detailId, List<DecisionTablesDetailCondition> list);
    List<DecisionTablesDetailCondition> insertDecisionTablesDetailCondition(Long detailId, List<DecisionTablesDetailCondition> list);
    boolean deleteByDecisionTablesDetailId(List<Long> detailIds);

    // runner
    List<DecisionTablesDetailCondition> queryByDecisionTablesDetailId(Long detailId);
    List<String> queryFieldEnByDetailIds(List<Long> detailIds);
}
