package com.fibo.ddp.common.service.strategyx.decisiontable;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesResult;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesResultVo;

/**
 * (DecisionTablesResult)表服务接口
 */
public interface DecisionTablesResultService extends IService<DecisionTablesResult> {
    DecisionTablesResultVo updateDecisionTablesResult(Long decisionTablesVersionId, DecisionTablesResultVo resultVo);
    DecisionTablesResultVo insertDecisionTablesResult(Long decisionTablesVersionId, DecisionTablesResultVo insert);
    boolean deleteByDecisionTablesVersionId(Long DecisionTablesVersionId);

    // runner
    DecisionTablesResultVo queryByDecisionTablesVersionId(Long decisionTablesVersionId);
}
