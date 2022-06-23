package com.fibo.ddp.common.service.strategyx.scorecard;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDimension;

import java.util.List;

public interface ScorecardDimensionService extends IService<ScorecardDimension> {

    // runner
    List<ScorecardDimension> getDimensionListByVersionId(Long versionId);
}
