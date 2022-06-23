package com.fibo.ddp.common.service.strategyx.collectionrule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationCondition;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;

import java.util.List;

public interface ListOperationConditionService extends IService<ListOperationCondition> {

    List<ListOperationCondition> queryByVersion(ListOperationVersion version);

    Long recursionSave(ListOperationCondition condition, ListOperationVersion version, Long blockId);

    boolean deleteByVersionId(Long versionId);
}
