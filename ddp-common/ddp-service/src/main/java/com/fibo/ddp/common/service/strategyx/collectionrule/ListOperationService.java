package com.fibo.ddp.common.service.strategyx.collectionrule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperation;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ListOperationService extends IService<ListOperation> {

    ListOperation queryById(Long id);

    PageInfo queryByEntity(QueryListParam<ListOperation> listParam);

    ListOperation insert(ListOperation listOperation);

    ListOperation update(ListOperation listOperation);

    boolean updateStatus(List<Long> ids, Integer status);

    boolean updateFolder(List<Long> ids, Long folderId);
}
