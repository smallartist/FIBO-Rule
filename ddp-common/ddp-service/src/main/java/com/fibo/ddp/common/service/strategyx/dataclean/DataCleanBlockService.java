package com.fibo.ddp.common.service.strategyx.dataclean;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanBlock;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;

import java.util.List;

public interface DataCleanBlockService extends IService<DataCleanBlock> {

    List<DataCleanBlock> queryByVersion(DataCleanVersion version);

    boolean deleteByVersionId(Long versionId);
}
