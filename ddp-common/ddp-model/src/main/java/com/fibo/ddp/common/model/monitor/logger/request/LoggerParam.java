package com.fibo.ddp.common.model.monitor.logger.request;

import com.fibo.ddp.common.model.common.BaseParam;
import lombok.Data;

@Data
public class LoggerParam extends BaseParam {

    private String searchKey;
    private String startDate;
    private String endDate;
}
