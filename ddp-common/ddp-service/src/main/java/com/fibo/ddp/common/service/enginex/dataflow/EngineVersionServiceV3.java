package com.fibo.ddp.common.service.enginex.dataflow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;

/**
 * 不同场景下的多个模型版本(EngineVersion)表服务接口
 *
 * @since 2021-12-22 14:39:33
 */
public interface EngineVersionServiceV3 extends IService<EngineVersion> {

    EngineVersion queryById(Long versionId);

    PageInfo queryList(QueryListParam<EngineVersion> param);
    List<EngineVersion> queryByEngineIds(Collection<Long> engineIds);
    List<EngineVersion> queryByEngineId(Long engineId);

    boolean addEngineVersion(EngineVersion param);

    boolean copyEngineVersion(Long versionId);

    /**
     * 假删，修改状态
     */
    boolean updateStatus(List<Long> ids, Integer status);
}
