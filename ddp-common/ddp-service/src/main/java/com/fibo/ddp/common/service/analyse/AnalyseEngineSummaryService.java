package com.fibo.ddp.common.service.analyse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.analyse.AnalyseEngineSummary;
import com.fibo.ddp.common.model.analyse.vo.AnalyseEngineSummaryVo;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;

import java.util.Map;

/**
 * (TAnalyseEngineSummary)表服务接口
 */
public interface AnalyseEngineSummaryService extends IService<AnalyseEngineSummary> {

    Map<String, AnalyseEngineSummaryVo>getAnalyseData(AnalyseRequestParam param);
}
