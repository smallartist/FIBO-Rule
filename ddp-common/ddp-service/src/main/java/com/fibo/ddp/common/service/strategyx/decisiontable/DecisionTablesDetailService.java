package com.fibo.ddp.common.service.strategyx.decisiontable;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesDetail;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesDetailVo;

import java.util.List;

/**
 * (DecisionTablesDetail)表服务接口
 */
public interface DecisionTablesDetailService  extends IService<DecisionTablesDetail> {
    boolean insertDecisionTablesDetail(Long decisionTablesVersionId, List<DecisionTablesDetailVo> list, Integer dimensionality);
    List<DecisionTablesDetailVo> updateDecisionTablesDetail(Long decisionTablesVersionId, List<DecisionTablesDetailVo> list, Integer dimensionality);
    boolean deleteByDecisionTablesVersionId(Long decisionTablesVersionId, Integer dimensionality);

    // runner
    List<DecisionTablesDetailVo> queryByDecisionTablesVersionId(Long decisionTablesVersionId, Integer dimensionality);
    List<Long> queryFieldIdByDecisionTablesVersionId(Long decisionTablesId);
}
