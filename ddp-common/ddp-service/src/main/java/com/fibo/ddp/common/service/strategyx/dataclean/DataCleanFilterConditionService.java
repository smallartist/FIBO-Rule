package com.fibo.ddp.common.service.strategyx.dataclean;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanFilterCondition;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;
import com.fibo.ddp.common.model.strategyx.dataclean.FilterCondEnum;

import java.util.List;

public interface DataCleanFilterConditionService extends IService<DataCleanFilterCondition> {

    List<DataCleanFilterCondition> queryByVersion(DataCleanVersion version);

    Long recursionSave(DataCleanFilterCondition condition, DataCleanVersion version, Long blockId, FilterCondEnum filterType);

    boolean deleteByVersionId(Long versionId);
}
