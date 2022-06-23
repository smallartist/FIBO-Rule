package com.fibo.ddp.common.service.analyse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.analyse.AnalyseRule;

import java.util.List;

/**
 * (AnalyseRule)表服务接口
 */
public interface AnalyseRuleService extends IService<AnalyseRule> ,AnalyseCommonService<List<AnalyseRule>>{

}
