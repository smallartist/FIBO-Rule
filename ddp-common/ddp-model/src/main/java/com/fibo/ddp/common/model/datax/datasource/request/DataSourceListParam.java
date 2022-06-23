package com.fibo.ddp.common.model.datax.datasource.request;


import com.fibo.ddp.common.model.common.PageDto;
import lombok.Data;

import java.util.List;

@Data
public class DataSourceListParam extends PageDto {

    /**
     * 数据源类型
     */
    private List<String> typeList;
}
