package com.fibo.ddp.common.service.strategyx.scorecard;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDetailCondition;

import java.util.List;

/**
 * 评分卡明细表的condition表(ScorecardDetailCondition)表服务接口
 */
public interface ScorecardDetailConditionService extends IService<ScorecardDetailCondition> {

    // runner
    List<ScorecardDetailCondition> getConditionListByDetailId(Integer detailId);
}
