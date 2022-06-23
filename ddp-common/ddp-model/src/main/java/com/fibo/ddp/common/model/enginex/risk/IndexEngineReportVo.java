package com.fibo.ddp.common.model.enginex.risk;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndexEngineReportVo implements Serializable {

    private static final long serialVersionUID = -1274492726714567316L;
    private String dayTime;
    private String monthTime;
    private Integer engineId;
    private String engineName;
    private Integer useNum;
}
