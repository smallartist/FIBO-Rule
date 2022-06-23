package com.fibo.ddp.common.service.analyse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.analyse.AnalyseDecisionTables;

import java.util.List;

/**
 * (AnalyseDecisionTables)表服务接口
 */
public interface AnalyseDecisionTablesService extends IService<AnalyseDecisionTables> ,AnalyseCommonService<List<List<AnalyseDecisionTables>>>{

}
