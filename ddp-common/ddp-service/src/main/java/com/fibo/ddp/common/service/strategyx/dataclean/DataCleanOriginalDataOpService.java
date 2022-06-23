package com.fibo.ddp.common.service.strategyx.dataclean;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanOriginalDataOp;
import com.fibo.ddp.common.model.strategyx.dataclean.DataCleanVersion;

/**
 * (DataCleanOriginalDataOp)表服务接口
 *
 * @author makejava
 * @since 2022-03-21 11:15:04
 */
public interface DataCleanOriginalDataOpService extends IService<DataCleanOriginalDataOp> {

    DataCleanOriginalDataOp queryByDataCleanVersion(DataCleanVersion version);

    void addOriginalDataOp(DataCleanOriginalDataOp originalDataOp);

    void updateDataOp(DataCleanVersion version, DataCleanOriginalDataOp originalDataOp);
}

