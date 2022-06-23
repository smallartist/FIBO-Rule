package com.fibo.ddp.common.model.strategyx.listlibrary.request;

import com.fibo.ddp.common.model.common.PageDto;
import lombok.Data;

@Data
public class ListDbDataParam extends PageDto {

    /**
     * 名单库id
     */
    private Long id;
}
