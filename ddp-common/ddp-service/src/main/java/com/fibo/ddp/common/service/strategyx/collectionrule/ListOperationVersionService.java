package com.fibo.ddp.common.service.strategyx.collectionrule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.collectionrule.ListOperationVersion;

import java.util.List;

public interface ListOperationVersionService  extends IService<ListOperationVersion> {

    List<ListOperationVersion> queryListByListOpId(Long listOpId);

    ListOperationVersion queryById(Long id);

    List<String> queryFieldEnByVersionId(Long versionId);

    int addVersionList(List<ListOperationVersion> versionList);

    boolean addVersion(ListOperationVersion version);

    boolean copyVersion(ListOperationVersion version);

    boolean updateVersion(ListOperationVersion version);

    boolean updateStatus(StatusParam statusParam);
}
