package com.fibo.ddp.common.model.enginex.risk.response.param;

import lombok.Data;

import java.util.List;

@Data
public class ListDbOutputResponse {

    /**
     * 名单库的统计信息
     */
    private List<NodeStrategyOutputResponse> statisticsOutputList;

    /**
     * 名单库信息
     */
    private List<ListDbInfoOutputResponse> listDbInfoOutput;
}
