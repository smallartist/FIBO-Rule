package com.fibo.ddp.common.service.strategyx.collectionrule;

import com.fibo.ddp.common.model.strategyx.collectionrule.ConditionAndOutPut;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;

import java.util.List;

public interface ConditionAndOutPutService {

    List<ConditionAndOutPut> queryByListOpVersion(ListOperationVersion version);

    boolean addBatch(ListOperationVersion version, List<ConditionAndOutPut> conditionAndOutPutList);

    boolean updateBatch(ListOperationVersion version, List<ConditionAndOutPut> conditionAndOutPutList);
    boolean deleteByVersionId(Long versionId);
}
