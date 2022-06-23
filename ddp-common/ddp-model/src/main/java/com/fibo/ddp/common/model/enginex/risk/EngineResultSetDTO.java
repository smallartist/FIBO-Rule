package com.fibo.ddp.common.model.enginex.risk;

import lombok.Data;

import java.util.Date;

/**
 * Created by niuge on 2021/11/15.
 */
@Data
public class EngineResultSetDTO {

    private Date callDate;

    private String result;

    private Long engineId;

    private Long engineVersion;

    private String engineName;

    private String engineCode;

    private Long organId;

    private Long  total;
}
