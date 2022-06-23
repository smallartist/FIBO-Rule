package com.fibo.ddp.common.service.strategyx.dataclean;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanOutput;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;

import java.util.List;

public interface DataCleanOutputService extends IService<DataCleanOutput> {

    List<DataCleanOutput> queryByVersion(DataCleanVersion version);

    boolean deleteByVersionId(Long versionId);
}
