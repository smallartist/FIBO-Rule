package com.fibo.ddp.common.model.monitor.decisionflow;

import lombok.Data;

import java.util.Date;

/**
 * Created by niuge on 2021/11/16.
 */
@Data
public class TMonitorStrategyDTO {
    private Date callDate;

    private String result;

    private Long engineId;

    private Long engineVersionId;

    private String engineName;

    private String engineCode;

    private Long organId;

    private Long strategyId;

    private String strategyName;

    private Long  total;
}
