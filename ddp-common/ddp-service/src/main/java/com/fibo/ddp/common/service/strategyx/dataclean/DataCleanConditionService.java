package com.fibo.ddp.common.service.strategyx.dataclean;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanCondition;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;

import java.util.List;

public interface DataCleanConditionService extends IService<DataCleanCondition> {

    List<DataCleanCondition> queryByVersion(DataCleanVersion version);

    Long recursionSave(DataCleanCondition condition, DataCleanVersion version, Long blockId);

    boolean deleteByVersionId(Long versionId);
}
