package com.fibo.ddp.common.service.strategyx.collectionrule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationFilterCondition;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;

import java.util.List;

public interface ListOperationFilterConditionService extends IService<ListOperationFilterCondition> {

    List<ListOperationFilterCondition> queryByVersion(ListOperationVersion version);

    Long recursionSave(ListOperationFilterCondition condition, ListOperationVersion version, Long blockId);

    boolean deleteByVersionId(Long versionId);
}
