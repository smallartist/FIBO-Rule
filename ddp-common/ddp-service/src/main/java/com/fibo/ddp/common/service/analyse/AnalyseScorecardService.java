package com.fibo.ddp.common.service.analyse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.analyse.AnalyseScorecard;

import java.util.List;

/**
 * (AnalyseScorecard)表服务接口
 */
public interface AnalyseScorecardService extends IService<AnalyseScorecard>,AnalyseCommonService<List<List<AnalyseScorecard>>> {

}
