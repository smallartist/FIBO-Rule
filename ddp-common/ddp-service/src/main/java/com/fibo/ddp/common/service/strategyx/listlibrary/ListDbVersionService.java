package com.fibo.ddp.common.service.strategyx.listlibrary;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDbVersion;

import java.util.List;

public interface ListDbVersionService extends IService<ListDbVersion> {

    ListDbVersion queryById(Long id);

    List<ListDbVersion> queryVersionListByListDbId(Long listDbId);

    int addVersionList(List<ListDbVersion> versionList);

    boolean addVersion(ListDbVersion version);

    boolean copyVersion(ListDbVersion version);

    boolean updateVersion(ListDbVersion version);

    boolean updateStatus(StatusParam StatusParam);
}
