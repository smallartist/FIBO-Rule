package com.fibo.ddp.common.service.analyse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.analyse.AnalyseDecisionResult;

import java.util.List;

/**
 * (AnalyseDecisionResult)表服务接口
 */
public interface AnalyseDecisionResultService extends IService<AnalyseDecisionResult>,AnalyseCommonService<List<AnalyseDecisionResult>> {

}
