package com.fibo.ddp.common.service.strategyx.dataclean;

import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanConditionAndOutPut;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;

import java.util.List;

public interface DataCleanConditionAndOutPutService {

    List<DataCleanConditionAndOutPut> queryByListOpVersion(DataCleanVersion version);

    boolean addBatch(DataCleanVersion version, List<DataCleanConditionAndOutPut> conditionAndOutPutList);

    boolean updateBatch(DataCleanVersion version, List<DataCleanConditionAndOutPut> conditionAndOutPutList);
    boolean deleteByVersionId(Long versionId);
}
