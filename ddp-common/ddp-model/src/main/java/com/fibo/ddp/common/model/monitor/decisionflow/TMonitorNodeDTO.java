package com.fibo.ddp.common.model.monitor.decisionflow;

import lombok.Data;

import java.util.Date;

/**
 * Created by niuge on 2021/11/19.
 */
@Data
public class TMonitorNodeDTO {

    private Date callDate;

    private Long engineId;

    private Long organId;

    private Long engineVersionId;

    private Long nodeId;

    private String nodeName;

    private Long total;
}
