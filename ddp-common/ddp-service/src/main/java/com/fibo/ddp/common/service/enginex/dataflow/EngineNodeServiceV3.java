package com.fibo.ddp.common.service.enginex.dataflow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;

import java.util.List;

/**
 * 存储版本模型的信息包括(使用的参数，节点的位置，节点的执行逻辑)(EngineNode)表服务接口
 * @since 2021-12-22 17:32:09
 */
public interface EngineNodeServiceV3 extends IService<EngineNode>{

    List<EngineNode> queryNodeListByVersion(Long versionId);

    boolean saveStartNode(Long versionId);

    boolean updateStatus(List<Long> ids, Integer status);
}
