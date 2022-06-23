package com.fibo.ddp.common.service.analyse;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.analyse.AnalyseEngineNode;

import java.util.List;

/**
 * (AnalyseEngineNode)表服务接口
 */
public interface AnalyseEngineNodeService extends IService<AnalyseEngineNode> ,AnalyseCommonService<List<AnalyseEngineNode>>{

}
