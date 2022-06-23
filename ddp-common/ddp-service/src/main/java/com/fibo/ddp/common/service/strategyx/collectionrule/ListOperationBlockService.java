package com.fibo.ddp.common.service.strategyx.collectionrule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationBlock;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;

import java.util.List;

public interface ListOperationBlockService extends IService<ListOperationBlock> {

    List<ListOperationBlock> queryByVersion(ListOperationVersion version);

    boolean deleteByVersionId(Long versionId);
}
