package com.fibo.ddp.common.service.enginex.dataflow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.enginex.dataflow.EngineVersionContent;
import com.fibo.ddp.common.model.enginex.dataflow.vo.EngineVersionContentVo;

import java.util.Collection;
import java.util.List;

/**
 * (EngineVersionContent)表服务接口
 * @since 2021-12-23 10:21:08
 */
public interface EngineVersionContentService extends IService<EngineVersionContent>{

    EngineVersionContentVo queryById(Long versionId);
    List<EngineVersionContentVo> queryByIds(Collection<Long> versionIds);
    boolean addVersionContent(EngineVersionContentVo versionContent);
    boolean updateVersionContent(EngineVersionContentVo versionContent);
    boolean deleteVersionContent(EngineVersionContent versionContent);
}
