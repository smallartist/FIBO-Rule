package com.fibo.ddp.common.service.strategyx.dataclean;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.strategyx.dataclean.DataClean;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DataCleanService extends IService<DataClean> {

    DataClean queryById(Long id);

    PageInfo queryByEntity(QueryListParam<DataClean> queryParam);

    DataClean queryByCode(String code,String versionCode);

    DataClean insert(DataClean dataClean);

    DataClean update(DataClean dataClean);

    boolean updateStatus(List<Long> ids, Integer status);

    boolean updateFolder(List<Long> ids, Long folderId);
}
