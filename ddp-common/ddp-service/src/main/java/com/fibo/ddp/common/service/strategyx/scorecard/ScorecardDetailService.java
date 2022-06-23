package com.fibo.ddp.common.service.strategyx.scorecard;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDetail;

import java.util.List;

/**
 * 评分卡明细表(ScorecardDetail)表服务接口
 */
public interface ScorecardDetailService extends IService<ScorecardDetail> {

    // runner
    List<ScorecardDetail> getDetailListByDimensionId(Integer dimensionId);

    List<ScorecardDetail> getDetailListByDimensionIds(List<Integer> dimensionIds);
}
