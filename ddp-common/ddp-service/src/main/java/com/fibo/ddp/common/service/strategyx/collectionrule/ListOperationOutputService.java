package com.fibo.ddp.common.service.strategyx.collectionrule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationOutput;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;

import java.util.List;

public interface ListOperationOutputService  extends IService<ListOperationOutput> {

    List<ListOperationOutput> queryByVersion(ListOperationVersion version);

    boolean deleteByVersionId(Long versionId);
}
