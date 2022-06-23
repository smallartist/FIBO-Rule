package com.fibo.ddp.common.service.strategyx.dataclean;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;

import java.util.List;

public interface DataCleanVersionService extends IService<DataCleanVersion> {

    List<DataCleanVersion> queryListByListOpId(Long listOpId);

    DataCleanVersion queryById(Long id,boolean needCache);


    DataCleanVersion queryByCode(Long dataCleanId ,String code);

    List<String> queryFieldEnByVersionId(Long versionId);

    int addVersionList(List<DataCleanVersion> versionList);

    boolean addVersion(DataCleanVersion version);

    boolean copyVersion(DataCleanVersion version);

    boolean updateVersion(DataCleanVersion version);

    boolean updateStatus(StatusParam statusParam);
}
